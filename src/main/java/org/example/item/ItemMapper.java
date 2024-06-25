package org.example.item;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ItemMapper {
/*    public Item toModel (ItemDTO dto, Long userId) {
        Item item = new Item();
        item.setUrl(dto.getUrl());
        item.setTags(dto.getTags());
        item.setUserId(userId);
        return item;
    }*/

    public ItemDTO toObj (Item item) {
        ItemDTO dto = new ItemDTO() {
            @Override
            public Set<String> getTags() {
                return null;
            }

            @Override
            public String getUrl() {
                return null;
            }

            @Override
            public void setTags(Set<String> tags) {

            }

            @Override
            public void setUrl(String url) {

            }
        };
        dto.setTags(item.getTags());
        dto.setUrl(item.getUrl());
        return dto;
    }
}
