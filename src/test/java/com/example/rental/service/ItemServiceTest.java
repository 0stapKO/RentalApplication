package com.example.rental.service;

import com.example.rental.entity.Item;
import com.example.rental.enums.ItemStatus;
import com.example.rental.exception.BusinessRuleException;
import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.repository.ItemRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private ItemService itemService;

    @Test
    void getItemByIdShouldReturnItem() {

        Long itemId = 1L;
        String itemName = "Item1";

        Item item = new Item();
        item.setId(itemId);
        item.setName(itemName);

        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));

        Item result = itemService.getItemById(itemId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(itemName, result.getName());
    }

    @Test
    void getItemByIdShouldThrowExceptionItemNotFound() {

        Long itemId = 1L;

        when(itemRepo.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.getItemById(itemId);
        });

        verify(itemRepo, times(1)).findById(itemId);
    }

    @Test
    void getAllItemsShouldReturnAllItems() {

        Long itemId1 = 1L;
        Long itemId2 = 2L;
        String itemName1 = "Item1";
        String itemName2 = "Item2";


        Item item1 = new Item();
        item1.setId(itemId1);
        item1.setName(itemName1);

        Item item2 = new Item();
        item2.setId(itemId2);
        item2.setName(itemName2);

        List<Item> items = List.of(item1, item2);

        when(itemRepo.searchFilterItems(null, null, null)).thenReturn(items);

        List<Item> result = itemService.getAllItems(null, null, null);

        assertEquals(2, result.size());
        assertEquals(itemId1, result.get(0).getId());
        assertEquals(itemName1, result.get(0).getName());
        assertEquals(itemId2, result.get(1).getId());
        assertEquals(itemName2, result.get(1).getName());
    }

    @Test
    void getAllItemsShouldReturnEmptyList() {

        when(itemRepo.searchFilterItems(null, null, null)).thenReturn(new ArrayList<>());

        List<Item> result = itemService.getAllItems(null, null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void addItemShouldAddItem() {

        Long itemId = 1L;
        String itemName = "Item1";

        Item item = new Item();
        item.setId(itemId);
        item.setName(itemName);

        when(itemRepo.save(item)).thenReturn(item);

        Item result = itemService.addItem(item);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(itemName, result.getName());
    }

    @Test
    void addItemShouldThrowExceptionItemAlreadyExists() {

        String inventoryNumber = "I#001";

        Item item1 = new Item();
        item1.setInventoryNumber(inventoryNumber);

        Item item2 = new Item();
        item2.setInventoryNumber(inventoryNumber);

        when(itemRepo.findByInventoryNumber(inventoryNumber)).thenReturn(Optional.of(item1));

        assertThrows(BusinessRuleException.class, () -> {
            itemService.addItem(item2);
        });

        verify(itemRepo, times(1)).findByInventoryNumber(inventoryNumber);
    }

    @Test
    void editItemShouldReturnEditedItem() {

        Long itemId = 1L;
        String itemName = "Item1";

        Item item = new Item();
        item.setId(itemId);
        item.setName(itemName);

        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));

        String newName = "NewItemName";
        Item newItem = new Item();
        newItem.setId(itemId);
        newItem.setName(newName);

        when(itemRepo.save(item)).thenReturn(newItem);

        Item result = itemService.editItem(itemId, newItem);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(newName, result.getName());
    }

    @Test
    void editItemShouldThrowExceptionItemNotFound() {

        Long itemId1 = 1L;
        String itemName = "Item1";

        Item item = new Item();
        item.setId(itemId1);
        item.setName(itemName);

        when(itemRepo.findById(itemId1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.editItem(itemId1, item);
        });

        verify(itemRepo, times(1)).findById(itemId1);
    }

    @Test
    void deleteItemDeleteShouldBeCalled() {

        Long itemId = 1L;
        String itemName = "Item1";

        Item item = new Item();
        item.setId(itemId);
        item.setName(itemName);

        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));

        itemService.deleteItem(itemId);

        verify(itemRepo, times(1)).delete(item);
    }

    @Test
    void deleteItemShouldThrowExceptionItemNotFound() {

        Long itemId1 = 1L;

        when(itemRepo.findById(itemId1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.deleteItem(itemId1);
        });

        verify(itemRepo, times(1)).findById(itemId1);
    }

    @Test
    void deleteItemShouldThrowExceptionItemIsRented() {

        Long itemId = 1L;
        String itemName = "Item1";

        Item item = new Item();
        item.setId(itemId);
        item.setName(itemName);
        item.setStatus(ItemStatus.RENTED);

        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(BusinessRuleException.class, () -> {
            itemService.deleteItem(itemId);
        });

        verify(itemRepo, times(1)).findById(itemId);
    }
}