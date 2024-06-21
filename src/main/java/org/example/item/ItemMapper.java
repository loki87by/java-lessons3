package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemMapper {
    private final CommentJPARepository commentsJPARepository;
    @Autowired
    public ItemMapper(CommentJPARepository commentsJPARepository) {
        this.commentsJPARepository=commentsJPARepository;
    }

    public ItemDTO toDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setDescription(item.getDescription());
        dto.setName(item.getName());
        dto.setBooked(item.isBooked());
        dto.setId(item.getId());
        List<CommentDTO> comments = commentsJPARepository.findAllByItemId(item.getId());
        //System.out.println("\u001B[38;5;44m" + "comments: "+comments.toString()+ "\u001B[0m");
        dto.setComments(comments);
        return dto;
    }

    public Item toModel(ItemDTO dto, Long owner) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setOwnerId(owner);
        return item;
    }

    public Item toModel(ItemDTO dto, Long owner, Long id) {
        Item item = new Item();
        item.setBooked(dto.isBooked());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setId(id);
        item.setOwnerId(owner);
        return item;
    }
}
