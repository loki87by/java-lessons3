package org.example.item;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private boolean isBooked;
    private List<String> feedbacks;
}
