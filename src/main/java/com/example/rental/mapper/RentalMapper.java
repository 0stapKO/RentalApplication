package com.example.rental.mapper;

import com.example.rental.dto.RentalResponse;
import com.example.rental.entity.Rental;

public class RentalMapper {

    public static RentalResponse toRentalResponse(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getUser() == null ? null : rental.getUser().getId(),
                rental.getItem() == null ? null : rental.getItem().getId(),
                rental.getStartDate(),
                rental.getExpectedReturnDate(),
                rental.getActualReturnDate(),
                rental.getStatus()
        );
    }
}
