package com.example.rental.controller;

import com.example.rental.dto.AuthResponse;
import com.example.rental.dto.LoginRequest;
import com.example.rental.dto.RegisterRequest;
import com.example.rental.security.JwtFilter;
import com.example.rental.service.AuthService;
import com.example.rental.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void registerShouldReturnToken() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest(
                "FirstName",
                "LastName",
                "user@email.com",
                "password"
        );

        String jsonBody = objectMapper.writeValueAsString(registerRequest);

        AuthResponse response = new AuthResponse("token");

        when(authService.register(registerRequest)).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void registerShouldReturn400NotValidEmail() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest(
                "FirstName",
                "LastName",
                "not valid email",
                "password"
        );

        String jsonBody = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.message").value("Email should be valid"));
    }

    @Test
    void registerShouldReturn400TooShortPassword() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest(
                "FirstName",
                "LastName",
                "user@email.com",
                "short"
        );

        String jsonBody = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.message").value("Password should be at least 8 characters long"));
    }

    @Test
    void loginShouldReturnToken() throws Exception{

        LoginRequest loginRequest = new LoginRequest(
                "user@email.com",
                "password"
        );

        String jsonBody = objectMapper.writeValueAsString(loginRequest);

        AuthResponse response = new AuthResponse("token");

        when(authService.login(loginRequest)).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }
}