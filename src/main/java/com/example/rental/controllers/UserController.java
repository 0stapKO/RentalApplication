package com.example.rental.controllers;

import com.example.rental.models.Rental;
import com.example.rental.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me/rentals")
    public ResponseEntity<List<Rental>> getUserRentals() {
        List<Rental> rentals = userService.getUserRentals();
        return new ResponseEntity<>(rentals, HttpStatus.FOUND);
    }

}
