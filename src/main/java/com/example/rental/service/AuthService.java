package com.example.rental.service;

import com.example.rental.dto.AuthResponse;
import com.example.rental.dto.LoginRequest;
import com.example.rental.dto.RegisterRequest;
import com.example.rental.enums.UserRole;
import com.example.rental.entity.User;
import com.example.rental.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {

        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = new User();

        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(UserRole.ROLE_USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepo.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        String token = jwtService.generateToken(loginRequest.getEmail());
        return new AuthResponse(token);
    }
}
