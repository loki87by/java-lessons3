package org.example.item;

import java.util.Set;

public class ItemMapper {
    public Item toModel (ItemDTO dto, Long userId) {
        Item item = new Item();
        item.setUrl(dto.getUrl());
        item.setTags(dto.getTags());
        item.setUserId(userId);
        return item;
    }

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
