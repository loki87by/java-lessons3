package org.example.item;

import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
/*    public Item toModel (ItemDTO dto, Long userId) {
        Item item = new Item();
        item.setUrl(dto.getUrl());
        item.setTags(dto.getTags());
        item.setUserId(userId);
        return item;
    }*/
    public Item addMetadata (Item item, UrlMetadataRetriever.UrlMetadata meta) {
        item.setHasImage(meta.isHasImage());
        //item.setUrl(meta.getNormalUrl());
        item.setDateResolved(meta.getDateResolved());
        item.setHasVideo(meta.isHasVideo());
        item.setMimeType(meta.getMimeType());
        item.setResolvedUrl(meta.getResolvedUrl());
        item.setTitle(meta.getTitle());
        return item;
    }

    /*public ItemDTO toObj (Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setUrl(item.getUrl());
        dto.setTags(item.getTags());
        dto.setResolvedUrl(item.getResolvedUrl());
        dto.setMimeType(item.getMimeType());
        dto.setTitle(item.getTitle());
        dto.setHasImage(item.isHasImage());
        dto.setHasVideo(item.isHasVideo());
        dto.setDateResolved(item.getDateResolved());
        dto.setUnread(item.isUnread());
        return dto;
    }*/
}
