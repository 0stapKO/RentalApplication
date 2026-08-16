package com.example.rental.models;

import com.example.rental.enums.RentalStatus;
import jakarta.persistence.*;
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
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    @Enumerated(EnumType.STRING)
    private RentalStatus status;

}
