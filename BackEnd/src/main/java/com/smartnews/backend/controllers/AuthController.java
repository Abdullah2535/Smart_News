package com.smartnews.backend.controllers;


import com.smartnews.backend.config.JwtConfig;
import com.smartnews.backend.dtos.JwtResponse;
import com.smartnews.backend.dtos.LoginRequest;
import com.smartnews.backend.dtos.UserDto;
import com.smartnews.backend.mappers.UserMapper;
import com.smartnews.backend.repositories.UserRepository;
import com.smartnews.backend.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://127.0.0.1:4200")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private JwtService jwtService;
    private final JwtConfig jwtConfig;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request,
                                             HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword())
        );
        var user = userRepository.findByUserName(request.getUserName()).orElseThrow();
       var accessToken =  jwtService.generateAccessToken(user);
        var refreshToken =  jwtService.generateRefreshToken(user);
        var cookie = new Cookie("refresh_token", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7days
        cookie.setSecure(true);
        response.addCookie(cookie);
        System.out.println("Setting access token cookie: " + accessToken);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void>handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    //    @PostMapping("/validate")
//    public boolean validate(@RequestHeader("Authorization") String authHeader){
//        System.out.println("Validate called" );
//     var token = authHeader.replace("Bearer ", "");
//      return jwtService.validateToken(authHeader);
//    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken" ) String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));

    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Integer) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);


    }
}
