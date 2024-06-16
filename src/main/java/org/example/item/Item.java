package org.example.item;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_id", nullable = false, updatable = false)
    private Long ownerId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false, length = 512)
    private String description;
    @Column(name = "is_booked", nullable = false)
    private boolean isBooked = false;
}
