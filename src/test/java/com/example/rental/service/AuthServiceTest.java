package com.example.rental.service;

import com.example.rental.dto.AuthResponse;
import com.example.rental.dto.LoginRequest;
import com.example.rental.dto.RegisterRequest;
import com.example.rental.entity.User;
import com.example.rental.exception.BusinessRuleException;
import com.example.rental.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldAddNewUserAndReturnToken() {

        String firstName = "FirstName";
        String lastName = "LastName";
        String password = "password";
        String email = "user@email.com";
        String encodedPassword = "encodedPassword";
        String token = "token";

        RegisterRequest registerRequest = new RegisterRequest(
                firstName,
                lastName,
                email,
                password
        );

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(jwtService.generateToken(email)).thenReturn(token);

        AuthResponse response = authService.register(registerRequest);

        assertEquals(token, response.token());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepo).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(firstName, savedUser.getFirstName());
        assertEquals(lastName, savedUser.getLastName());
        assertEquals(email, savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());
    }

    @Test
    void registerShouldThrowExceptionUserAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest(
                "FirstName",
                "LastName",
                "exist@email.com",
                "password"
        );

        when(userRepo.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(BusinessRuleException.class, () -> {
            authService.register(registerRequest);
        });

        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void loginShouldReturnToken() {

        String email = "user@email.com";
        String password = "password";
        String token = "token";

        LoginRequest loginRequest = new LoginRequest(
                email,
                password
        );

        when(jwtService.generateToken(email)).thenReturn(token);

        AuthResponse response = authService.login(loginRequest);

        assertEquals(token, response.token());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}