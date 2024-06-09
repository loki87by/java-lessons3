package org.example.itemRequest;

import org.example.item.ItemDTO;
import org.example.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ItemRequestService {
    ItemRequestRepository itemRequestRepository;
    Utils utils;
    ItemRequestMapper itemRequestMapper;

    @Autowired
    public ItemRequestService(ItemRequestRepository itemRequestRepository, Utils utils, ItemRequestMapper itemRequestMapper) {
        this.itemRequestRepository = itemRequestRepository;
        this.utils = utils;
        this.itemRequestMapper = itemRequestMapper;
    }

    public List<ItemRequestDTO> findAll() {
        HashMap<Long, ItemRequest> itemRequests = itemRequestRepository.findAll();
        return utils.getListDTO(itemRequests.values().stream().toList(), item -> itemRequestMapper.toDTO(item));
    }

    public List<ItemRequestDTO> findMy(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findByUserId(userId);
        return utils.getListDTO(itemRequests, item -> itemRequestMapper.toDTO(item));
    }

    public ItemRequestDTO save(Long userId, String reqItem) {
        HashMap<Long, ItemRequest> itemRequests = itemRequestRepository.findAll();
        Long hash = Math.abs(Long.parseLong(String.valueOf(itemRequests.hashCode())));
        Long id = utils.getUniqueId(itemRequests, hash);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setReqItem(reqItem);
        itemRequest.setId(id);
        itemRequest.setAuthor(userId);
        itemRequestRepository.save(itemRequest);
        return itemRequestMapper.toDTO(itemRequest);
    }

    public String addItem(Long id, ItemDTO dto, Long userId) {
        ItemRequestDTO irDto = itemRequestRepository.addItem(userId, dto, id);
        Long itemId = irDto.getRespItem();
        return "Добавлено, доступ по адресу:\nhttp://localhost:8080/items/" + itemId;
    }

    public ItemRequestDTO update(Long userId, Long id, String reqItem) {
        return itemRequestRepository.update(userId, id, reqItem);
    }

    public String delete(Long userId, Long id) {
        return itemRequestRepository.delete(userId, id);
    }
}
