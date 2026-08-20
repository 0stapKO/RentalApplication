package com.example.rental.controller;

import com.example.rental.dto.RentalCreateRequest;
import com.example.rental.entity.Rental;
import com.example.rental.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Rental>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Rental> addRental(@Valid @RequestBody RentalCreateRequest rentalCreateRequest) {
        Rental newRental = rentalService.addRental(rentalCreateRequest);
        return new ResponseEntity<>(newRental, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Rental> returnRental(@PathVariable Long id) {
        Rental returnedRental = rentalService.returnRental(id);
        return new ResponseEntity<>(returnedRental, HttpStatus.OK);
    }

}
