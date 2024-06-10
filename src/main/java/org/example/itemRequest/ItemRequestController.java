package org.example.itemRequest;

import org.example.item.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itemRequests")
public class ItemRequestController {
    ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @GetMapping("/all")
    public List<ItemRequestDTO> getAll() {
        return itemRequestService.findAll();
    }

    @GetMapping("")
    public List<ItemRequestDTO> getMy(@RequestHeader("X-Later-User-Id") Long userId) {
        return itemRequestService.findMy(userId);
    }

    @PostMapping("")
    public ItemRequestDTO save(@RequestHeader("X-Later-User-Id") Long userId,
                               @RequestParam("reqItem") String reqItem) {
        return itemRequestService.save(userId, reqItem);
    }

    @PostMapping("/{id}")
    public String setItem(@RequestHeader("X-Later-User-Id") Long userId,
                          @PathVariable (name = "id") Long id,
                          @RequestBody ItemDTO dto) {
        return itemRequestService.addItem(id, dto, userId);
    }

    @PatchMapping("/{id}")
    public ItemRequestDTO update(@RequestHeader("X-Later-User-Id") Long userId,
                                 @PathVariable (name = "id") Long id,
                                 @RequestParam(value = "reqItem") String reqItem) {
        return itemRequestService.update(userId, id, reqItem);
    }

    @DeleteMapping("/{id}")
    public String delete(@RequestHeader("X-Later-User-Id") Long userId,
                         @PathVariable (name = "id") Long id) {
        return itemRequestService.delete(userId, id);
    }
}
