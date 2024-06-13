package org.example.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final EntityManager entityManager;
    @Autowired
    public ItemRepositoryImpl(EntityManager entityManager) {
        this.entityManager=entityManager;
    }
    @Override
    public List<Item> findByUserId(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> cr = cb.createQuery(Item.class);
        Root<Item> root = cr.from(Item.class);
        cr.select(root).where(cb.equal(root.get("owner_id"), userId));
        return entityManager.createQuery(cr).getResultList();
    }

    private Item findItemById(Long itemId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> cr = cb.createQuery(Item.class);
        Root<Item> root = cr.from(Item.class);
        cr.select(root).where(cb.equal(root.get("id"), itemId));
        return entityManager.createQuery(cr).getSingleResult();
    }

    @Override
    public Item findUserItem(Long userId, Long itemId) {
        Item item = findItemById(itemId);

        if (item.getOwner().equals(userId)) {
            return item;
        } else {
            throw new SecurityException("Недостаточно прав или запись отсутствует в БД");
        }
    }

    @Override
    public void save(Item item) {
        entityManager.persist(item);
    }

    @Override
    public void delete(Long itemId) {
        Item item = findItemById(itemId);

        if (item != null) {
            entityManager.remove(item);
        } else {
            throw new NoSuchElementException("Запрашиваемый ресурс не найден.");
        }
    }
}
