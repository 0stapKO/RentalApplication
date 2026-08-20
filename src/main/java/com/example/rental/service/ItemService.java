package com.example.rental.service;

import com.example.rental.enums.ItemCategory;
import com.example.rental.enums.ItemStatus;
import com.example.rental.exception.BusinessRuleException;
import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.entity.Item;
import com.example.rental.repository.ItemRepo;
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

    public List<Item> getAllItems(String name, ItemStatus status, ItemCategory category) {
        return itemRepo.searchFilterItems(name, status, category);
    }

    public Item addItem(Item item) {
        return itemRepo.save(item);
    }

    public Item editItem(Long id, Item newItem) {
        Item existingItem = itemRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Item with id " + id + " was not found."));

        existingItem.setName(newItem.getName());
        existingItem.setDescription(newItem.getDescription());
        existingItem.setInventoryNumber(newItem.getInventoryNumber());
        existingItem.setCategory(newItem.getCategory());

        return itemRepo.save(existingItem);
    }

    public void deleteItem(Long id) {
        Item item = itemRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Item with id " + id + " was not found."));

        if(item.getStatus() == ItemStatus.RENTED)
            throw new BusinessRuleException("Cannot delete rented item");

        itemRepo.delete(item);
    }
}
