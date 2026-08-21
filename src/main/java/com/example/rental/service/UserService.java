package com.example.rental.service;

import com.example.rental.dto.UserResponse;
import com.example.rental.entity.Rental;
import com.example.rental.entity.User;
import com.example.rental.enums.UserRole;
import com.example.rental.exception.BusinessRuleException;
import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.mapper.UserMapper;
import com.example.rental.repository.UserRepo;
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
    private final UserRepo userRepo;

    public List<Rental> getUserRentals() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User currentUser = principal.getUser();
        return rentalRepo.findAllByUserId(currentUser.getId());
    }

    public UserResponse makeUserAdmin(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User with id " + id + " does not exist"));

        if(user.getRole() == UserRole.ROLE_ADMIN)
            throw new BusinessRuleException("User with id " + id + " is already an admin");

        user.setRole(UserRole.ROLE_ADMIN);

        User userAdmin = userRepo.save(user);

        return UserMapper.toUserResponse(userAdmin);
    }
}
