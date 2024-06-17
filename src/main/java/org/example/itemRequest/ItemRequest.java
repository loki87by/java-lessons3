package org.example.itemRequest;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_id", nullable = false, updatable = false)
    private Long ownerId;
    @Column(name = "req_item_name", nullable = false)
    private String reqItemName;
    @Column(name = "resp_item_id")
    private Long respItemId = null;
}
