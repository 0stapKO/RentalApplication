package com.example.rental.service;

import com.example.rental.entity.Rental;
import com.example.rental.entity.User;
import com.example.rental.security.UserPrincipal;
import com.example.rental.repository.RentalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RentalRepo rentalRepo;

    public List<Rental> getUserRentals() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User currentUser = principal.getUser();
        return rentalRepo.findAllByUserId(currentUser.getId());
    }
}
