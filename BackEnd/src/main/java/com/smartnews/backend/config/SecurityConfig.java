package com.smartnews.backend.config;



import com.smartnews.backend.entities.Role;
import com.smartnews.backend.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    public final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
//@Bean
//public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
//                                                   PasswordEncoder passwordEncoder) {
//    return authentication -> {
//        UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());
//        if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
//            throw new BadCredentialsException("Invalid credentials");
//        }
//        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
//    };
//}


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Stateless sessions (token-based authentication)
        //Disable CSRF
        //Authorize
        http
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors((Customizer.withDefaults()))
                .authorizeHttpRequests(
                        c -> c.
                                requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/users").permitAll()
                                //user for crawler
                                .requestMatchers(HttpMethod.POST,"/news").permitAll()
                                //user for ai
                                .requestMatchers(HttpMethod.PUT,"/news/ai").permitAll()
                                .requestMatchers(HttpMethod.GET,"/news/ai").permitAll()
                                .requestMatchers(HttpMethod.GET,"preferences/").authenticated()
                                .requestMatchers(HttpMethod.POST,"/news/userP").authenticated()
                                .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/refresh").permitAll()
                                .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c->{
                            c.authenticationEntryPoint(
                                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                           c.accessDeniedHandler(((request, response, accessDeniedException) ->
                                    response.setStatus(HttpStatus.FORBIDDEN.value()  )));
                        }
                        );
        return http.build();
    }
//coldplay admin password
// 2. Define the CORS configuration bean
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("http://localhost:4200"));

    // Allow common HTTP methods
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

    // Allow all headers (Authorization, Content-Type, etc.)
    configuration.setAllowedHeaders(List.of("*"));

    // Allow cookies or auth headers to be sent
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}

