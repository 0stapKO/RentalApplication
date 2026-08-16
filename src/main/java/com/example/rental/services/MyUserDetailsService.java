package com.example.rental.services;

import com.example.rental.exceptions.ResourceNotFoundException;
import com.example.rental.models.User;
import com.example.rental.models.UserPrincipal;
import com.example.rental.repos.UserRepo;
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
