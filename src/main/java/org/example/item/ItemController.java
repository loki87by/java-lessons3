package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl  itemServiceImpl;
    @Autowired
    public ItemController(ItemServiceImpl  itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @GetMapping("")
    public List<Item> getItems (@RequestHeader("X-Later-User-Id") Long userId) {
        return itemServiceImpl.findByUserId(userId);
    }

    @PostMapping("")
    public Item setItem (@RequestHeader("X-Later-User-Id") Long userId, @RequestParam(name = "url") String url) {
        Item item = new Item();
        item.setUserId(userId);
        item.setUrl(url);
        return itemServiceImpl.save(item);
    }

    /*@DeleteMapping("/{itemId}")
    public void deleteItem (@RequestHeader("X-Later-User-Id") Long userId, @PathVariable(name = "itemId") Long itemId) {
        itemServiceImpl.deleteByUserIdAndItemId(userId, itemId);
    }*/
}
