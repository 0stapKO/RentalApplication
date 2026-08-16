package com.example.rental.repos;

import com.example.rental.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepo extends JpaRepository<Rental, Long> {

    List<Rental> findAllByUserId(Long id);
}
