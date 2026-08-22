package com.example.rental.controller;

import com.example.rental.dto.RentalCreateRequest;
import com.example.rental.entity.Rental;
import com.example.rental.security.JwtFilter;
import com.example.rental.service.JwtService;
import com.example.rental.service.RentalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentalController.class)
@AutoConfigureMockMvc(addFilters = false)
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RentalService rentalService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void addRentalShouldReturnNewRental() throws Exception {

        Long itemId = 1L;
        Long rentalId = 1L;
        LocalDate expectedReturnDate = LocalDate.now().plusDays(1);

        RentalCreateRequest rentalCreateRequest = new RentalCreateRequest(
                itemId,
                expectedReturnDate
        );

        String jsonBody = objectMapper.writeValueAsString(rentalCreateRequest);

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setExpectedReturnDate(expectedReturnDate);

        when(rentalService.addRental(rentalCreateRequest)).thenReturn(rental);

        mockMvc.perform(post("/api/rentals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(rentalId))
                .andExpect(jsonPath("$.expectedReturnDate").value(expectedReturnDate.toString()));
    }

    @Test
    void addRentalShouldReturn400ReturnDateNotInFuture() throws Exception {

        Long itemId = 1L;
        Long rentalId = 1L;
        LocalDate expectedReturnDate = LocalDate.now();

        RentalCreateRequest rentalCreateRequest = new RentalCreateRequest(
                itemId,
                expectedReturnDate
        );

        String jsonBody = objectMapper.writeValueAsString(rentalCreateRequest);

        mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.message").value("Expected return date must be in the future"));
    }

    @Test
    void returnRental() throws Exception {

        Long rentalId = 1L;

        Rental rental = new Rental();
        rental.setId(rentalId);

        when(rentalService.returnRental(rentalId)).thenReturn(rental);

        mockMvc.perform(post("/api/rentals/{id}/return", rentalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rentalId));
    }
}