package com.example.rental.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCreateRequest {

    @NotNull(message = "Item ID is required")
    private Long itemId;

    @NotNull(message = "Expected return date is required")
    @Future(message = "Expected return date must be in the future")
    private LocalDate expectedReturnDate;
}
