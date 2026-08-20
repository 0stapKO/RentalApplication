package com.example.rental.entity;

import com.example.rental.enums.ItemCategory;
import com.example.rental.enums.ItemStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Category cannot be empty")
    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Column(unique = true)
    @NotBlank(message = "Inventory number cannot be empty")
    private String inventoryNumber;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

}
