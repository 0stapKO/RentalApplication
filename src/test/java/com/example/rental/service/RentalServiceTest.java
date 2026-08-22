package com.example.rental.service;

import com.example.rental.dto.RentalCreateRequest;
import com.example.rental.entity.Item;
import com.example.rental.entity.Rental;
import com.example.rental.entity.User;
import com.example.rental.enums.ItemStatus;
import com.example.rental.enums.RentalStatus;
import com.example.rental.exception.BusinessRuleException;
import com.example.rental.repository.ItemRepo;
import com.example.rental.repository.RentalRepo;
import com.example.rental.security.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    RentalRepo rentalRepo;
    @Mock
    ItemRepo itemRepo;

    @InjectMocks
    RentalService rentalService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addRentalShouldCreateNewRental() {

        Long itemId = 1L;
        LocalDate expectedReturnDate = LocalDate.of(2026, 7, 22);

        RentalCreateRequest rentalCreateRequest = new RentalCreateRequest(
                itemId,
                expectedReturnDate
        );

        Item item = new Item();
        item.setId(itemId);
        item.setStatus(ItemStatus.AVAILABLE);

        User user = new User();
        user.setEmail("user@email.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUser()).thenReturn(user);

        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));

        rentalService.addRental(rentalCreateRequest);

        ArgumentCaptor<Rental> rentalCaptor = ArgumentCaptor.forClass(Rental.class);

        verify(rentalRepo).save(rentalCaptor.capture());

        Rental rental = rentalCaptor.getValue();

        assertEquals(ItemStatus.RENTED, item.getStatus());
        assertEquals(item, rental.getItem());
        assertEquals(user, rental.getUser());
        assertEquals(expectedReturnDate, rental.getExpectedReturnDate());
        assertEquals(RentalStatus.ACTIVE, rental.getStatus());
    }

    @Test
    void addRentalShouldThrowExceptionItemIsAlreadyRented() {

        Long itemId = 1L;
        LocalDate expectedReturnDate = LocalDate.of(2026, 7, 22);

        RentalCreateRequest rentalCreateRequest = new RentalCreateRequest(
                itemId,
                expectedReturnDate
        );

        Item item = new Item();
        item.setId(itemId);
        item.setStatus(ItemStatus.RENTED);

        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(BusinessRuleException.class, () -> {
            rentalService.addRental(rentalCreateRequest);
        });
    }

    @Test
    void returnRentalShouldUpdateRentalAndItemStatus() {

        Long rentalId = 1L;
        Long userId = 1L;

        Item item = new Item();

        User user = new User();
        user.setId(userId);

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setItem(item);
        rental.setUser(user);
        rental.setStatus(RentalStatus.ACTIVE);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUser()).thenReturn(user);

        when(rentalRepo.findById(rentalId)).thenReturn(Optional.of(rental));

        Rental resultRental = rentalService.returnRental(1L);

        assertEquals(RentalStatus.RETURNED, resultRental.getStatus());
        assertNotNull(resultRental.getActualReturnDate());
        assertEquals(ItemStatus.AVAILABLE, item.getStatus());
    }
    @Test
    void returnRentalShouldThrowExceptionOtherUsersRental() {

        Long rentalId = 1L;
        Long userId1 = 1L;
        Long userId2 = 2L;

        Item item = new Item();

        User currentUser = new User();
        currentUser.setId(userId1);

        User rentalUser = new User();
        rentalUser.setId(userId2);

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setItem(item);
        rental.setUser(rentalUser);
        rental.setStatus(RentalStatus.ACTIVE);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUser()).thenReturn(currentUser);

        when(rentalRepo.findById(rentalId)).thenReturn(Optional.of(rental));

        assertThrows(BusinessRuleException.class, () -> {
            rentalService.returnRental(rentalId);
        });
    }
}