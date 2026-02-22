package com.smartnews.backend.services;

import com.smartnews.backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       var user = userRepository.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        return new User(
                user.getUserName(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
