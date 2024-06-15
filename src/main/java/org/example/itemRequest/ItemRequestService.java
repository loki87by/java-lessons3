package org.example.itemRequest;

import org.example.item.ItemDTO;
import org.example.user.UserInMemoryRepository;
import org.example.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemRequestService {
    ItemRequestRepository itemRequestRepository;
    Utils utils;
    ItemRequestMapper itemRequestMapper;
    UserInMemoryRepository userInMemoryRepository;

    @Autowired
    public ItemRequestService(@Qualifier("itemRequestRepositoryImpl") ItemRequestRepository itemRequestRepository,
                              Utils utils,
                              ItemRequestMapper itemRequestMapper,
                              UserInMemoryRepository userInMemoryRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.utils = utils;
        this.itemRequestMapper = itemRequestMapper;
        this.userInMemoryRepository = userInMemoryRepository;
    }

    public List<ItemRequestDTO> findAll() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAll();
        return utils.getListDTO(itemRequests, item -> itemRequestMapper.toDTO(item));
    }

    public List<ItemRequestDTO> findMy(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findByUserId(userId);
        return utils.getListDTO(itemRequests, item -> itemRequestMapper.toDTO(item));
    }

    public ItemRequestDTO save(Long userId, String reqItem) {
        boolean isRealUser = userInMemoryRepository.checkUser(userId);

        if (!isRealUser) {
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден. Проверьте заголовки запроса.");
        }
        List<ItemRequest> itemRequests = itemRequestRepository.findAll();
        Long hash = Math.abs(Long.parseLong(String.valueOf(itemRequests.hashCode())));
        HashMap<Long, ItemRequest> mapa = new HashMap<>();
        for (ItemRequest ir: itemRequests) {
            mapa.put(ir.getId(), ir);
        }
        Long id = utils.getUniqueId(mapa, hash);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setReqItem(reqItem);
        itemRequest.setId(id);
        itemRequest.setAuthor(userId);
        itemRequestRepository.save(itemRequest);
        return itemRequestMapper.toDTO(itemRequest);
    }

    public String addItem(Long id, ItemDTO dto, Long userId) {
        boolean isRealUser = userInMemoryRepository.checkUser(userId);

        if (!isRealUser) {
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден. Проверьте заголовки запроса.");
        }
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
