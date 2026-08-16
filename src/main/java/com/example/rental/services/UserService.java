package com.example.rental.services;

import com.example.rental.models.Rental;
import com.example.rental.models.User;
import com.example.rental.models.UserPrincipal;
import com.example.rental.repos.RentalRepo;
import com.example.rental.repos.UserRepo;
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
