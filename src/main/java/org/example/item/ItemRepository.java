package org.example.item;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ItemRepository {
    List<Item> findByUserId(long userId);
    Item save(Item item);
    void deleteByUserIdAndItemId(long userId, long itemId);
}
