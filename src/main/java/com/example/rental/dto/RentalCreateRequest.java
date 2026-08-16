package com.example.rental.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalCreateRequest {

    @NotNull(message = "Item ID is required")
    private Long itemId;

    @NotNull(message = "Expected return date is required")
    @Future(message = "Expected return date must be in the future")
    private LocalDate expectedReturnDate;
}
