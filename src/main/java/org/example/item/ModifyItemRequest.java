package org.example.item;

import lombok.Data;

import java.util.Set;

@Data
public class ModifyItemRequest {
    Long userId;
    Long itemId;
    Boolean unread = null;
    boolean replaceTags;
    Set<String> tags = null;
    String url = null;
}
