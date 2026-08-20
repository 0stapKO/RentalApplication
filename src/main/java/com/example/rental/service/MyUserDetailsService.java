package com.example.rental.service;

import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.entity.User;
import com.example.rental.security.UserPrincipal;
import com.example.rental.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByEmail(username).orElseThrow(() ->
                new ResourceNotFoundException("User with email " + username + " does not exist"));

        return new UserPrincipal(user);
    }
}
