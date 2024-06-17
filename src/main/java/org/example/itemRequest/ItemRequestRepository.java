package org.example.itemRequest;

import org.example.item.ItemDTO;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ItemRequestRepository {
    List<ItemRequest> findAll();

    List<ItemRequest> findByUserId(Long userId);

    ItemRequest save(ItemRequest itemRequest);

    ItemRequestDTO addItem(Long userId, ItemDTO dto, Long iRid);

    ItemRequestDTO update(Long userId, Long id, String reqItem);

    String delete(Long userId, Long itemRequestId);
}
