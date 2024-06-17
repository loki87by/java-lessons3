package org.example.itemRequest;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.item.Item;
import org.example.item.ItemDTO;
import org.example.item.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Repository
public class ItemRequestRepositoryImpl implements ItemRequestRepository {
    private final EntityManager entityManager;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;
    private final ItemRequestJPARepository itemRequestJPARepository;

    @Autowired
    public ItemRequestRepositoryImpl(EntityManager entityManager,
                                     ItemRequestJPARepository itemRequestJPARepository,
                                     ItemRequestMapper itemRequestMapper,
                                     ItemMapper itemMapper) {
        this.entityManager = entityManager;
        this.itemRequestMapper = itemRequestMapper;
        this.itemMapper = itemMapper;
        this.itemRequestJPARepository = itemRequestJPARepository;
    }

    @Override
    public List<ItemRequest> findAll() {
        return itemRequestJPARepository.findAll();
    }

    @Override
    public List<ItemRequest> findByUserId(Long userId) {
        return itemRequestJPARepository.findByOwnerId(userId);
    }

    @Override
    @Transactional
    public ItemRequest save(ItemRequest itemRequest) {
        return itemRequestJPARepository.saveAndFlush(itemRequest);
    }

    @Override
    @Transactional
    public ItemRequestDTO addItem(Long userId, ItemDTO dto, Long iRid) {
        Item newItem = itemMapper.toModel(dto, userId);
        entityManager.persist(newItem);
        Long id = newItem.getId();
        ItemRequest itemRequest = itemRequestJPARepository.findById(iRid).orElse(null);
        assert itemRequest != null;
        //System.out.println("\u001B[38;5;44m" + "itemRequest: "+itemRequest.toString()+ "\u001B[0m");
        if (itemRequest.getRespItemId() != null) {
            throw new IllegalArgumentException("Этот запрос уже получил ответ.");
        }
        itemRequest.setRespItemId(id);
        entityManager.merge(itemRequest);
        return itemRequestMapper.toDTO(itemRequest);
    }

    @Override
    @Transactional
    public ItemRequestDTO update(Long userId, Long id, String reqItem) {
        ItemRequest itemRequest = itemRequestJPARepository.findById(id).orElse(null);

        if (itemRequest == null) {
            throw new NoSuchElementException("Не найдено запросов с таким id.");
        }
        boolean isOwner = Objects.equals(itemRequest.getOwnerId(), userId);

        if (!isOwner) {
            throw new SecurityException("У вас нет прав для данной операции.");
        } else {
            itemRequest.setReqItemName(reqItem);
            ItemRequest newitemRequest = itemRequestJPARepository.saveAndFlush(itemRequest);
            return itemRequestMapper.toDTO(newitemRequest);
        }
    }

    @Override
    @Transactional
    public String delete(Long userId, Long itemRequestId) {
        ItemRequest itemRequest = itemRequestJPARepository.findById(itemRequestId).orElse(null);

        if (itemRequest == null) {
            throw new NoSuchElementException("Не найдено запросов с таким id.");
        }
        boolean isOwner = Objects.equals(itemRequest.getOwnerId(), userId);

        if (isOwner) {
            entityManager.remove(itemRequest);
            return "Запись удалена.";
        }
        throw new SecurityException("Error: нет прав для операции.");
    }
}
