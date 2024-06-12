package org.example.item;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl {
    private final List<Item> items = new ArrayList<>();
    private final EntityManager entityManager;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.itemMapper = itemMapper;
    }

    public List<ItemDTO> findByUserId(long userId) {
        return items.stream().filter(item -> item.getUserId() == userId).toList()
                .stream()
                .map(itemMapper::toObj)
                .collect(Collectors.toList());
    }

    public ItemDTO save(Item item) {
        entityManager.persist(item);
        return itemMapper.toObj(item);
    }

    public void deleteByUserIdAndItemId(long userId, long itemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == itemId && items.get(i).getUserId() == userId) {
                items.remove(i);
                break;
            }
        }
    }

}
