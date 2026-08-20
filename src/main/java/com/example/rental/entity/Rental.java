package com.example.rental.entity;

import com.example.rental.enums.RentalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private LocalDate startDate;

    @NotNull(message = "Expected return date cannot be empty")
    private LocalDate expectedReturnDate;

    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

}
