package com.example.rental.repos;

import com.example.rental.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepo extends JpaRepository<Rental, Long> {
}
