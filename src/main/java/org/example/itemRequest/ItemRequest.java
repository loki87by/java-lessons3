package org.example.itemRequest;

import lombok.Data;

@Data
public class ItemRequest {
    private Long id;
    private Long author;
    private String reqItem;
    private Long respItem = null;
}
