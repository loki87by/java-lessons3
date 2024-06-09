package org.example.item;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
public class ItemRepository {
    private static final HashMap<Long, Item> items = new HashMap<>();

    public HashMap<Long, Item> findAll() {
        return items;
    }

    public List<Item> findByUserId(Long userId) {
        return items.values().stream().filter(i -> Objects.equals(i.getOwner(), userId)).toList();
    }

    public Item findUserItem(Long userId, Long itemId) {
        Item item = items.get(itemId);

        if (Objects.equals(item.getOwner(), userId)) {
            return item;
        } else {
            return new Item();
        }
    }

    public void save(Item item) {
        items.put(item.getId(), item);
    }

    public void delete(Long itemId) {
        items.remove(itemId);
    }
}
