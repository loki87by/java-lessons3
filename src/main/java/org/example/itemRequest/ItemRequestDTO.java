package org.example.itemRequest;

import lombok.Data;

@Data
public class ItemRequestDTO {
    private Long id;
    private String reqItem;
    private Long respItem = null;
}
