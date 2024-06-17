package org.example.itemRequest;

import org.example.item.ItemDTO;
import org.example.user.UserRepository;
import org.example.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final Utils utils;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;

    @Autowired
    public ItemRequestService(@Qualifier("itemRequestRepositoryImpl") ItemRequestRepository itemRequestRepository,
                              Utils utils,
                              ItemRequestMapper itemRequestMapper,
                              @Qualifier("userRepositoryImpl") UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.utils = utils;
        this.itemRequestMapper = itemRequestMapper;
        this.userRepository = userRepository;
    }

    public List<ItemRequestDTO> findAll() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAll();
        return utils.getListDTO(itemRequests, itemRequestMapper::toDTO);
    }

    public List<ItemRequestDTO> findMy(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findByUserId(userId);
        return utils.getListDTO(itemRequests, itemRequestMapper::toDTO);
    }

    public ItemRequestDTO save(Long userId, String reqItem) {
        boolean isRealUser = userRepository.checkUser(userId);

        if (!isRealUser) {
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден. Проверьте заголовки запроса.");
        }
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setReqItemName(reqItem);
        itemRequest.setOwnerId(userId);
        ItemRequest current = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.toDTO(current);
    }

    public String addItem(Long id, ItemDTO dto, Long userId) {
        boolean isRealUser = userRepository.checkUser(userId);

        if (!isRealUser) {
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден. Проверьте заголовки запроса.");
        }
        ItemRequestDTO irDto = itemRequestRepository.addItem(userId, dto, id);
        Long itemId = irDto.getRespItemId();
        return "Добавлено, доступ по адресу:\nhttp://localhost:8080/items/" + itemId;
    }

    public ItemRequestDTO update(Long userId, Long id, String reqItem) {
        return itemRequestRepository.update(userId, id, reqItem);
    }

    public String delete(Long userId, Long id) {
        return itemRequestRepository.delete(userId, id);
    }
}
