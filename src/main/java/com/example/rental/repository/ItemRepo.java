package com.example.rental.repository;

import com.example.rental.enums.ItemCategory;
import com.example.rental.enums.ItemStatus;
import com.example.rental.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE " +
            "(CAST(:name AS String) IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', CAST(:name AS String), '%'))) AND " +
            "(:status IS NULL OR i.status = :status) AND " +
            "(:category IS NULL OR i.category = :category)")
    List<Item> searchFilterItems(
            @Param("name") String name,
            @Param("status") ItemStatus status,
            @Param("category")ItemCategory category
            );

    Optional<Item> findByInventoryNumber(String inventoryNumber);
}
