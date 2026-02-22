package com.smartnews.backend.controllers;

import com.smartnews.backend.dtos.RegisterUserRequest;
import com.smartnews.backend.dtos.UserDto;
import com.smartnews.backend.entities.Role;
import com.smartnews.backend.mappers.UserMapper;
import com.smartnews.backend.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


//    @GetMapping("")
//    public Iterable<UserDto> getAllUsers(@RequestHeader(required = false, name = "x-auth-token")String authToken,
//                                         @RequestParam(required = false,defaultValue = "", name = "sortBy") String sortBy) {
//
//        System.out.println(authToken);
//
//
//        if (!Set.of("name","userName").contains(sortBy)) {
//            sortBy = "name";
//        }
//        return userRepository.findAll(Sort.by(sortBy))
//                .stream()
//             //   .map(user -> new UserDto(user.getId(), user.getName(), user.getUserName()))
//                .map(userMapper::toUserDto)
//                .toList();
//    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {

        var user= userRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        //  var userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }
    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request,
                                          UriComponentsBuilder uriBuilder) {
        if (userRepository.existsByUserName(request.getUserName()) )
            return ResponseEntity.badRequest().body(
                    Map.of("userName","UserName is already registered ")
            );

        var user = userMapper.toEntity(request);
        //for later dont forget
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        var uri   =uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        var userDto = userMapper.toUserDto(user);
        return ResponseEntity.created(uri).body(userDto);
    }
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDto> updateUser (@PathVariable(name = "id")Long id
//            ,@RequestBody UpdateUserRequest request){
//        var user = userRepository.findById(id).orElse(null);
//        if(user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        userMapper.updateUser(request,user);
//        userRepository.save(user);
//
//        return ResponseEntity.ok(userMapper.toUserDto(user));
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
//    @PostMapping("/{id}/change-password")
//    public ResponseEntity<Void> changePassword(@PathVariable Long id,
//                                               @RequestBody ChangePasswordRequest request){
//        var user = userRepository.findById(id).orElse(null);
//        if(user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if(!user.getPassword().equals(request.getOldPassword())) {
//            return new  ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        user.setPassword(request.getNewPassword());
//        userRepository.save(user);
//        return ResponseEntity.noContent().build();
//
//    }




}
