package com.example.rental.services;

import com.example.rental.exceptions.ResourceNotFoundException;
import com.example.rental.models.Item;
import com.example.rental.repos.ItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepo itemRepo;

    public Item getItemById(Long id) {
        return itemRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Item with id " + id + " was not found."));
    }

    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }

    public Item addItem(Item item) {
        return itemRepo.save(item);
    }

}
