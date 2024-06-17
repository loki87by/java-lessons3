package org.example.item;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final EntityManager entityManager;
    private final ItemJPARepository itemJPARepository;
    @Autowired
    public ItemRepositoryImpl(EntityManager entityManager,
                              ItemJPARepository itemJPARepository) {
        this.entityManager=entityManager;
        this.itemJPARepository = itemJPARepository;
    }
    @Override
    public List<Item> findByUserId(Long userId) {
        return itemJPARepository.findAllByOwnerIdIs(userId);
    }

    private Optional<Item> findItemById(Long itemId) {
        return itemJPARepository.findById(itemId);
    }

    @Override
    public Item findUserItem(Long userId, Long itemId) {
        Item item = findItemById(itemId).orElse(null);

        if (item != null && item.getOwnerId().equals(userId)) {
            return item;
        } else {
            throw new SecurityException("Недостаточно прав или запись отсутствует в БД");
        }
    }

    @Transactional
    @Override
    public Item save(Item item) {
        return itemJPARepository.saveAndFlush(item);
    }

    @Override
    @Transactional
    public void delete(Long itemId) {
        Item item = findItemById(itemId).orElse(null);

        if (item != null) {
            entityManager.remove(item);
        } else {
            throw new NoSuchElementException("Запрашиваемый ресурс не найден.");
        }
    }

    @Override
    public HashMap<Long, Item> findAll() {
        List<Item> items = itemJPARepository.findAll();
        HashMap<Long, Item> mapa = new HashMap<>();
        for (Item item: items) {
            mapa.put(item.getId(), item);
        }
        return mapa;
    }
}
