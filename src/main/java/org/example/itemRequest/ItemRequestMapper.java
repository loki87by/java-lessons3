package org.example.itemRequest;

import org.springframework.stereotype.Service;

@Service
public class ItemRequestMapper {

    public ItemRequestDTO toDTO(ItemRequest itemRequest) {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setRespItemId(itemRequest.getRespItemId());
        dto.setReqItemName(itemRequest.getReqItemName());
        dto.setId(itemRequest.getId());
        return dto;
    }

    /*public ItemRequest toModel(ItemRequestDTO dto, Long author) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(dto.getId());
        itemRequest.setReqItem(dto.getReqItem());
        itemRequest.setRespItem(dto.getRespItem());
        itemRequest.setAuthor(author);
        return itemRequest;
    }*/
}