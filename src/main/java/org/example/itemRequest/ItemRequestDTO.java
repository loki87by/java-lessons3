package org.example.itemRequest;

import lombok.Data;

@Data
public class ItemRequestDTO {
    private Long id;
    private String reqItemName;
    private Long respItemId = null;
}
