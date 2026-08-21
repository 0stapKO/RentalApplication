package com.example.rental.controller;

import com.example.rental.dto.UserResponse;
import com.example.rental.entity.Rental;
import com.example.rental.entity.User;
import com.example.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/make-admin")
    public ResponseEntity<UserResponse> makeUserAdmin(@PathVariable Long id) {
        UserResponse user = userService.makeUserAdmin(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
