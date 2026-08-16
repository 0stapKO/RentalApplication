package com.example.rental.models;

import com.example.rental.enums.ItemStatus;
import jakarta.persistence.*;
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
    private String name;
    private String description;
    private String category;
    @Column(unique = true)
    private String inventoryNumber;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

}
