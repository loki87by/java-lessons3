package org.example.item;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.HashMap;
import java.util.List;

@RepositoryRestResource
public interface ItemRepository {
    List<Item> findByUserId(Long userId);

    Item findUserItem(Long userId, Long itemId);

    Item save(Item item);

    void delete(Long itemId);

    HashMap<Long, Item> findAll();
}
