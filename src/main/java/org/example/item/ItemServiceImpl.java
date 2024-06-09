package org.example.item;

import org.example.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    Utils utils;
    ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, Utils utils, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.utils = utils;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<ItemDTO> findAll() {
        HashMap<Long, Item> items = itemRepository.findAll();
        return utils.getListDTO(items.values().stream().toList(), item -> itemMapper.toDTO(item));
    }

    @Override
    public List<ItemDTO> findByUserId(Long userId) {
        List<Item> items = itemRepository.findByUserId(userId);
        return utils.getListDTO(items, item -> itemMapper.toDTO(item));
    }

    @Override
    public ItemDTO getItem(Long userId, Long itemId) {
        Item item = itemRepository.findUserItem(userId, itemId);
        return itemMapper.toDTO(item);
    }

    @Override
    public List<ItemDTO> search(Long userId, String text) {
        List<ItemDTO> items = findAll();
        List<ItemDTO> finded = new ArrayList<>();
        for(ItemDTO item: items) {

            if((item.getName().contains(text) || item.getDescription().contains(text)) && !item.isBooked()) {
                finded.add(item);
            }
        }
        return finded;
    }

    @Override
    public ItemDTO save(ItemDTO dto, Long userId) {
        HashMap<Long, Item> items = itemRepository.findAll();
        Long hash = Math.abs(Long.parseLong(String.valueOf(items.hashCode())));
        Long id = utils.getUniqueId(items, hash);
        Item item = itemMapper.toModel(dto, userId, id);
        itemRepository.save(item);
        return dto;
    }

    private boolean isOwner(Long userId, Long itemId) {
        HashMap<Long, Item> items = itemRepository.findAll();
        return Objects.equals(items.get(itemId).getOwner(), userId);
    }

    @Override
    public String deleteByUserIdAndItemId(Long userId, Long itemId) {
        HashMap<Long, Item> items = itemRepository.findAll();
        int length = items.size();

        if (isOwner(userId, itemId)) {
            itemRepository.delete(itemId);
            int newLength = items.size();

            if (length > newLength) {
                return "success";
            } else {
                return "error: user not found";
            }
        } else {
            return "error: access denied";
        }
    }

    @Override
    public String update(ItemDTO dto, Long userId, Long itemId) {
        Item item = itemMapper.toModel(dto, userId, itemId);

        if (isOwner(userId, itemId)) {
            itemRepository.save(item);
            return "success";
        } else {
            return "error: access denied";
        }
    }
}
