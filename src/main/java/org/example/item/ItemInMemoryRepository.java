package org.example.item;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
public class ItemInMemoryRepository implements ItemRepository{
    private static final HashMap<Long, Item> items = new HashMap<>();

    public HashMap<Long, Item> findAll() {
        return items;
    }

    @Override
    public List<Item> findByUserId(Long userId) {
        return items.values().stream().filter(i -> Objects.equals(i.getOwnerId(), userId)).toList();
    }

    @Override
    public Item findUserItem(Long userId, Long itemId) {
        Item item = items.get(itemId);

        if (Objects.equals(item.getOwnerId(), userId)) {
            return item;
        } else {
            return new Item();
        }
    }

    @Override
    public Item save(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void delete(Long itemId) {
        items.remove(itemId);
    }
}
