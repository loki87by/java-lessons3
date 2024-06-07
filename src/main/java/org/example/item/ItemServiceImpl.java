package org.example.item;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemServiceImpl implements ItemRepository {
    private final List<Item> items = new ArrayList<>();

    @Override
    public List<Item> findByUserId(long userId) {
        return items.stream().filter(item -> item.getUserId() == userId).toList();
    }

    @Override
    public Item save(Item item) {
        int hash = items.hashCode();
        item.setId(Long.parseLong(String.valueOf(hash + 1)));
        items.add(item);
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == itemId && items.get(i).getUserId() == userId) {
                items.remove(i);
                break;
            }
        }
    }
}
