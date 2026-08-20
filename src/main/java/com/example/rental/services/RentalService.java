package com.example.rental.services;

import com.example.rental.dto.RentalCreateRequest;
import com.example.rental.enums.ItemStatus;
import com.example.rental.enums.RentalStatus;
import com.example.rental.exceptions.BusinessRuleException;
import com.example.rental.exceptions.ResourceNotFoundException;
import com.example.rental.models.Item;
import com.example.rental.models.Rental;
import com.example.rental.models.User;
import com.example.rental.models.UserPrincipal;
import com.example.rental.repos.ItemRepo;
import com.example.rental.repos.RentalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalService {

    private final RentalRepo rentalRepo;
    private final ItemRepo itemRepo;
    private final ApplicationContext applicationContext;

    public List<Rental> getAllRentals() {
        return rentalRepo.findAll();
    }

    public Rental addRental(RentalCreateRequest rentalCreateRequest) {
        Long itemId = rentalCreateRequest.getItemId();
        Item item = itemRepo.findById(itemId).orElseThrow(() ->
                new ResourceNotFoundException("Item with id " + itemId + " was not found"));

        if(item.getStatus() != ItemStatus.AVAILABLE)
            throw new BusinessRuleException("Item is not available for rent");

        item.setStatus(ItemStatus.RENTED);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        Rental rental = new Rental(
                null,
                user,
                item,
                LocalDate.now(),
                rentalCreateRequest.getExpectedReturnDate(),
                null,
                RentalStatus.ACTIVE
        );

        return rentalRepo.save(rental);
    }

    public Rental returnRental(Long id) {
        Rental rental = rentalRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Rental with id " + id + " was not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User currentUser = principal.getUser();
        if(!currentUser.getId().equals(rental.getUser().getId()))
            throw new BusinessRuleException("User can return only their own rentals");

        if(rental.getStatus() != RentalStatus.ACTIVE)
            throw new BusinessRuleException("Rental is not active");

        rental.setActualReturnDate(LocalDate.now());
        rental.setStatus(RentalStatus.RETURNED);

        Item item = rental.getItem();
        item.setStatus(ItemStatus.AVAILABLE);

        return rental;
    }

}
