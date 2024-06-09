package org.example.item;

import lombok.Data;

import java.util.List;

@Data
public class ItemDTO {
    private Long id = null;
    private String name;
    private String description;
    private boolean isBooked;
    private List<String> feedbacks;
}
