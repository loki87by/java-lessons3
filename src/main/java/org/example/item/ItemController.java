package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl  itemRepository;
    @Autowired
    public ItemController(ItemServiceImpl  itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("")
    public List<Item> getItems (@RequestHeader("X-Later-User-Id") Long userId) {
        return itemRepository.findByUserId(userId);
    }

    @PostMapping("")
    public Item setItem (@RequestHeader("X-Later-User-Id") Long userId, @RequestParam(name = "url") String url) {
        Item item = new Item();
        item.setUserId(userId);
        item.setUrl(url);
        return itemRepository.save(item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem (@RequestHeader("X-Later-User-Id") Long userId, @PathVariable(name = "itemId") Long itemId) {
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
    }
}
