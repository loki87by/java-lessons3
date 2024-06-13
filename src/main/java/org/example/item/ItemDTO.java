package org.example.item;

import lombok.Data;

@Data
public class ItemDTO {
    private Long id = null;
    private String name;
    private String description;
    private boolean isBooked = false;
}
