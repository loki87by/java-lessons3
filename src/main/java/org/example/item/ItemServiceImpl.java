package org.example.item;

import org.example.user.UserJPARepository;
import org.example.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final Utils utils;
    private final ItemMapper itemMapper;
    private final UserJPARepository userRepository;

    @Autowired
    public ItemServiceImpl(@Qualifier("itemRepositoryImpl") ItemRepository itemRepository,
                           UserJPARepository userRepository,
                           Utils utils,
                           ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.utils = utils;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<ItemDTO> findAll() {
        HashMap<Long, Item> items = itemRepository.findAll();
        return utils.getListDTO(items.values().stream().toList(), itemMapper::toDTO);
    }

    @Override
    public List<ItemDTO> findByUserId(Long userId) {
        List<Item> items = itemRepository.findByUserId(userId);
        return utils.getListDTO(items, itemMapper::toDTO);
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
        for (ItemDTO item : items) {

            if ((item.getName().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase())) && !item.isBooked()) {
                finded.add(item);
            }
        }
        return finded;
    }

    private void checkEmptyDtoData(ItemDTO dto, Long userId) {
        boolean isRealUser = userRepository.existsById(userId);

        if (!isRealUser) {
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден. Проверьте заголовки запроса.");
        }

        if (dto.getName().isEmpty() || dto.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Название и описание не должны быть пустыми.");
        }
    }

    @Override
    public ItemDTO save(ItemDTO dto, Long userId) {
        checkEmptyDtoData(dto, userId);
        Item item = itemMapper.toModel(dto, userId);
        Item newItem = itemRepository.save(item);
        return itemMapper.toDTO(newItem);
    }

    private boolean isOwner(Long userId, Long itemId) {
        HashMap<Long, Item> items = itemRepository.findAll();
        return Objects.equals(items.get(itemId).getOwnerId(), userId);
    }

    @Override
    public String deleteByUserIdAndItemId(Long userId, Long itemId) {

        if (isOwner(userId, itemId)) {
            itemRepository.delete(itemId);
            return "success";
        }
        throw new SecurityException("У вас нет прав для удаления данного материала.");
    }

    @Override
    public ItemDTO update(ItemDTO dto, Long userId, Long itemId) {
        checkEmptyDtoData(dto, userId);
        Item item = itemMapper.toModel(dto, userId, itemId);

        if (isOwner(userId, itemId)) {
            Item updatedItem = itemRepository.save(item);
            return itemMapper.toDTO(updatedItem);
        }
        throw new SecurityException("У вас нет прав для редактирования данного материала.");
    }
}
