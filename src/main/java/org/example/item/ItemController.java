package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/all")
    public List<ItemDTO> getAllItems() {
        return itemService.findAll();
    }

    @GetMapping("")
    public List<ItemDTO> getMyItems(@RequestHeader("X-Later-User-Id") Long userId) {
        return itemService.findByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItem(@RequestHeader("X-Later-User-Id") Long userId,
                           @PathVariable(name = "itemId") Long itemId) {
        //need throws if id==null or item undefined
        return itemService.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDTO> searchItem(@RequestHeader("X-Later-User-Id") Long userId,
                                    @RequestParam("text") String text) {
        //need add case ignore
        return itemService.search(userId, text);
    }

    @PostMapping("")
    public ItemDTO setItem(@RequestHeader("X-Later-User-Id") Long userId, @RequestBody ItemDTO dto) {
        //need add check not empty name & description
        //need add check user in users
        return itemService.save(dto, userId);
    }

    @PatchMapping("/{itemId}")
    public String updItem(@RequestHeader("X-Later-User-Id") Long userId,
                          @RequestBody ItemDTO dto,
                          @PathVariable(name = "itemId") Long itemId) {
        //need add check not empty name & description
        return itemService.update(dto, userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public String deleteItem(@RequestHeader("X-Later-User-Id") Long userId, @PathVariable(name = "itemId") Long itemId) {
        return itemService.deleteByUserIdAndItemId(userId, itemId);
    }
}
