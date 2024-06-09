package org.example.itemRequest;

import org.example.item.Item;
import org.example.item.ItemDTO;
import org.example.item.ItemMapper;
import org.example.item.ItemRepository;
import org.example.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
public class ItemRequestRepository {
    private final HashMap<Long, ItemRequest> itemRequests = new HashMap<>();
    ItemRepository itemRepository;
    Utils utils;
    ItemMapper itemMapper;
    ItemRequestMapper itemRequestMapper;

    @Autowired
    public ItemRequestRepository(ItemRepository itemRepository,
                                 Utils utils,
                                 ItemMapper itemMapper,
                                 ItemRequestMapper itemRequestMapper) {
        this.itemRepository = itemRepository;
        this.utils = utils;
        this.itemMapper = itemMapper;
        this.itemRequestMapper = itemRequestMapper;
    }

    public HashMap<Long, ItemRequest> findAll() {
        return itemRequests;
    }

    public List<ItemRequest> findByUserId(Long userId) {
        return itemRequests.values().stream().filter(i -> Objects.equals(i.getAuthor(), userId)).toList();
    }

    public void save(ItemRequest itemRequest) {
        itemRequests.put(itemRequest.getId(), itemRequest);
    }

    public ItemRequestDTO addItem(Long userId, ItemDTO dto, Long iRid) {
        HashMap<Long, Item> items = itemRepository.findAll();
        Long hash = Math.abs(Long.parseLong(String.valueOf(items.hashCode())));
        Long id = utils.getUniqueId(items, hash);
        Item newItem = itemMapper.toModel(dto, userId, id);
        itemRepository.save(newItem);
        ItemRequest itemRequest = itemRequests.get(iRid);
        itemRequest.setRespItem(id);
        itemRequests.put(iRid, itemRequest);
        return itemRequestMapper.toDTO(itemRequest);
    }

/*    private boolean isOwner(Long id, Long userId) {
        ItemRequest itemRequest = itemRequests.get(id);
        Long author = itemRequest.getAuthor();

        return Objects.equals(author, userId);
    }*/

    public ItemRequestDTO update(Long userId, Long id, String reqItem) {
        ItemRequest itemRequest = itemRequests.get(id);
        boolean isOwner = Objects.equals(itemRequest.getAuthor(), userId);

        if (isOwner) {
            return new ItemRequestDTO();
        } else {
            itemRequest.setReqItem(reqItem);
            itemRequests.put(id, itemRequest);
            return itemRequestMapper.toDTO(itemRequest);
        }
    }

    public String delete(Long userId, Long itemRequestId) {
        ItemRequest itemRequest = itemRequests.get(itemRequestId);
        boolean isOwner = Objects.equals(itemRequest.getAuthor(), userId);

        if (isOwner) {
            itemRequests.remove(itemRequestId);
            return "Запись удалена.";
        }
        return "Error: нет прав для операции.";
    }
}
