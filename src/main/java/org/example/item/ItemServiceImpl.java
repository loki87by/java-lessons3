package org.example.item;

import org.example.user.UserInMemoryRepository;
import org.example.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {
    ItemInMemoryRepository itemInMemoryRepository;
    Utils utils;
    ItemMapper itemMapper;
    UserInMemoryRepository userInMemoryRepository;

    @Autowired
    public ItemServiceImpl(ItemInMemoryRepository itemInMemoryRepository,
                           Utils utils,
                           ItemMapper itemMapper,
                           UserInMemoryRepository userInMemoryRepository) {
        this.itemInMemoryRepository = itemInMemoryRepository;
        this.utils = utils;
        this.itemMapper = itemMapper;
        this.userInMemoryRepository = userInMemoryRepository;
    }

    @Override
    public List<ItemDTO> findAll() {
        HashMap<Long, Item> items = itemInMemoryRepository.findAll();
        return utils.getListDTO(items.values().stream().toList(), item -> itemMapper.toDTO(item));
    }

    @Override
    public List<ItemDTO> findByUserId(Long userId) {
        List<Item> items = itemInMemoryRepository.findByUserId(userId);
        return utils.getListDTO(items, item -> itemMapper.toDTO(item));
    }

    @Override
    public ItemDTO getItem(Long userId, Long itemId) {
        Item item = itemInMemoryRepository.findUserItem(userId, itemId);
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
        boolean isRealUser = userInMemoryRepository.checkUser(userId);

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
        HashMap<Long, Item> items = itemInMemoryRepository.findAll();
        Long hash = Math.abs(Long.parseLong(String.valueOf(items.hashCode())));
        Long id = utils.getUniqueId(items, hash);
        Item item = itemMapper.toModel(dto, userId, id);
        itemInMemoryRepository.save(item);
        return itemMapper.toDTO(item);
    }

    private boolean isOwner(Long userId, Long itemId) {
        HashMap<Long, Item> items = itemInMemoryRepository.findAll();
        return Objects.equals(items.get(itemId).getOwner(), userId);
    }

    @Override
    public String deleteByUserIdAndItemId(Long userId, Long itemId) {
        HashMap<Long, Item> items = itemInMemoryRepository.findAll();
        int length = items.size();

        if (isOwner(userId, itemId)) {
            itemInMemoryRepository.delete(itemId);
            int newLength = items.size();

            if (length > newLength) {
                return "success";
            }
            throw new NoSuchElementException("С таким id(" + itemId + ") ничего не найдено.");
        }
        throw new SecurityException("У вас нет прав для удаления данного материала.");
    }

    @Override
    public String update(ItemDTO dto, Long userId, Long itemId) {
        checkEmptyDtoData(dto, userId);
        Item item = itemMapper.toModel(dto, userId, itemId);

        if (isOwner(userId, itemId)) {
            itemInMemoryRepository.save(item);
            return "success";
        }
        throw new SecurityException("У вас нет прав для редактирования данного материала.");
    }
}
