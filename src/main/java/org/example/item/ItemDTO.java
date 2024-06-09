package org.example.item;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDTO {
    private Long id = null;
    private String name;
    private String description;
    private boolean isBooked = false;
    private List<String> feedbacks = new ArrayList<>();
}
