package com.example.rental.dto;

import com.example.rental.entity.Item;
import com.example.rental.entity.User;
import com.example.rental.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalResponse {

    private Long id;
    private Long userId;
    private Long itemId;
    private LocalDate startDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private RentalStatus status;

}
