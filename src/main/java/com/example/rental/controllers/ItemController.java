package com.example.rental.controllers;

import com.example.rental.enums.ItemCategory;
import com.example.rental.enums.ItemStatus;
import com.example.rental.models.Item;
import com.example.rental.services.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ItemStatus status,
            @RequestParam(required = false) ItemCategory category)
            {
        List<Item> allItems = itemService.getAllItems(name, status, category);
        return new ResponseEntity<>(allItems, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Item> addItem(@Valid @RequestBody Item item) {
        Item newItem = itemService.addItem(item);
        return new ResponseEntity<>(newItem, HttpStatus.CREATED);
    }

}
