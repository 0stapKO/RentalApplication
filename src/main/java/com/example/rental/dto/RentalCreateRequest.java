package com.example.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RentalCreateRequest {

    private Long itemId;
    private LocalDate expectedReturnDate;
}
