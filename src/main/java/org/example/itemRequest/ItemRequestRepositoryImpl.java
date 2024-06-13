package org.example.itemRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.item.Item;
import org.example.item.ItemDTO;
import org.example.item.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ItemRequestRepositoryImpl implements ItemRequestRepository {
    private final EntityManager entityManager;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemRequestRepositoryImpl(EntityManager entityManager, ItemRequestMapper itemRequestMapper, ItemMapper itemMapper) {
        this.entityManager = entityManager;
        this.itemRequestMapper = itemRequestMapper;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<ItemRequest> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemRequest> cr = cb.createQuery(ItemRequest.class);
        Root<ItemRequest> root = cr.from(ItemRequest.class);
        cr.select(root);
        return entityManager.createQuery(cr).getResultList();
    }

    @Override
    public List<ItemRequest> findByUserId(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemRequest> cr = cb.createQuery(ItemRequest.class);
        Root<ItemRequest> root = cr.from(ItemRequest.class);
        cr.select(root).where(cb.equal(root.get("id"), userId));
        return entityManager.createQuery(cr).getResultList();
    }

    @Override
    public void save(ItemRequest itemRequest) {
        entityManager.merge(itemRequest);
    }

    private ItemRequest getItemRequestById(Long iRid) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemRequest> cr = cb.createQuery(ItemRequest.class);
        Root<ItemRequest> root = cr.from(ItemRequest.class);
        cr.select(root).where(cb.equal(root.get("id"), iRid));
        return entityManager.createQuery(cr).getSingleResult();
    }

    @Override
    public ItemRequestDTO addItem(Long userId, ItemDTO dto, Long iRid) {
        Item newItem = itemMapper.toModel(dto, userId);
        entityManager.persist(newItem);
        ItemRequest itemRequest = getItemRequestById(iRid);
        entityManager.merge(itemRequest);
        return itemRequestMapper.toDTO(itemRequest);
    }

    @Override
    public ItemRequestDTO update(Long userId, Long id, String reqItem) {
        ItemRequest itemRequest = getItemRequestById(id);
        boolean isOwner = Objects.equals(itemRequest.getAuthor(), userId);

        if (!isOwner) {
            return new ItemRequestDTO();
        } else {
            entityManager.merge(itemRequest);
            return itemRequestMapper.toDTO(itemRequest);
        }
    }

    @Override
    public String delete(Long userId, Long itemRequestId) {
        ItemRequest itemRequest = getItemRequestById(itemRequestId);
        boolean isOwner = Objects.equals(itemRequest.getAuthor(), userId);

        if (isOwner) {
            entityManager.remove(itemRequestId);
            return "Запись удалена.";
        }
        throw new SecurityException("Error: нет прав для операции.");
    }
}
