package com.example.rental.repository;

import com.example.rental.entity.Item;
import com.example.rental.enums.ItemCategory;
import com.example.rental.enums.ItemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class ItemRepoTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15.3");

    @Autowired
    private ItemRepo itemRepo;

    @Test
    void contextLoads() {
        assertTrue(postgreSQLContainer.isRunning());
    }

    @BeforeEach
    void setUp() {
        itemRepo.deleteAll();
    }

    @Test
    void findByInventoryNumberFound() {

        String inventoryNumber = "INV12345";
        ItemCategory category = ItemCategory.CLOTHES;
        String name = "Name";
        Item item = new Item();
        item.setInventoryNumber(inventoryNumber);
        item.setCategory(category);
        item.setName(name);
        itemRepo.save(item);

        Item foundItem = itemRepo.findByInventoryNumber(inventoryNumber).orElse(null);

        assertNotNull(foundItem);
        assertEquals(inventoryNumber, foundItem.getInventoryNumber());
    }

    @Test
    void findByInventoryNumberNotFound() {

        String inventoryNumber = "INV12345";
        String wrongInventoryNumber = "INV12354";
        ItemCategory category = ItemCategory.CLOTHES;
        String name = "Name";
        Item item = new Item();
        item.setInventoryNumber(inventoryNumber);
        item.setCategory(category);
        item.setName(name);
        itemRepo.save(item);

        Item foundItem = itemRepo.findByInventoryNumber(wrongInventoryNumber).orElse(null);

        assertNull(foundItem);
    }

    @Test
    void searchFilterItems() {
        String inventoryNumber1 = "INV1";
        ItemCategory category = ItemCategory.CLOTHES;
        String name1 = "Item1";
        ItemStatus status = ItemStatus.AVAILABLE;
        Item item1 = new Item();
        item1.setInventoryNumber(inventoryNumber1);
        item1.setCategory(category);
        item1.setName(name1);
        item1.setStatus(status);
        itemRepo.save(item1);

        String inventoryNumber2 = "INV2";
        ItemCategory category2 = ItemCategory.MUSIC;
        String name2 = "Item2";
        Item item2 = new Item();
        item2.setInventoryNumber(inventoryNumber2);
        item2.setCategory(category2);
        item2.setName(name2);
        item2.setStatus(status);
        itemRepo.save(item2);

        String inventoryNumber3 = "INV3";
        String name3 = "Item3";
        Item item3 = new Item();
        item3.setInventoryNumber(inventoryNumber3);
        item3.setCategory(category);
        item3.setName(name3);
        item3.setStatus(status);
        itemRepo.save(item3);

        List<Item> searchByStatus = itemRepo.searchFilterItems(null, status, null);
        assertEquals(3, searchByStatus.size());

        List<Item> searchByCategory = itemRepo.searchFilterItems(null, null, category);
        assertEquals(2, searchByCategory.size());

        List<Item> searchByName = itemRepo.searchFilterItems(name2, null, null);
        assertEquals(1, searchByName.size());
        assertEquals(name2, searchByName.getFirst().getName());

        List<Item> searchByAll = itemRepo.searchFilterItems(name1, status, category);
        assertEquals(1, searchByAll.size());
        assertEquals(name1, searchByAll.getFirst().getName());
    }
}