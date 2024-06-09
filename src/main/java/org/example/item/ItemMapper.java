package org.example.item;

import org.springframework.stereotype.Service;

@Service
public class ItemMapper {

    public ItemDTO toDTO (Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setDescription(item.getDescription());
        dto.setFeedbacks(item.getFeedbacks());
        dto.setName(item.getName());
        dto.setBooked(item.isBooked());
        dto.setId(item.getId());
        return dto;
    }

    public Item toModel (ItemDTO dto, Long owner, Long id) {
        Item item = new Item();
        item.setBooked(dto.isBooked());
        item.setName(dto.getName());
        item.setFeedbacks(dto.getFeedbacks());
        item.setDescription(dto.getDescription());
        item.setId(id);
        item.setOwner(owner);
        return item;
    }
}
