package org.example.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {
    List<ItemDTO> findAll();

    List<ItemDTO> findByUserId(Long userId);

    ItemDTO getItem(Long userId, Long itemId);

    List<ItemDTO> search(Long userId, String text);

    ItemDTO save(ItemDTO dto, Long userId);

    String deleteByUserIdAndItemId(Long userId, Long itemId);

    String update(ItemDTO dto, Long userId, Long itemId);
}
