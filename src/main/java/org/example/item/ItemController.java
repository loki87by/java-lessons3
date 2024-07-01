package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @GetMapping("")
    public List<ItemDTO> getItems(@RequestHeader("X-Later-User-Id") Long userId,
                                  @RequestParam(name = "state", defaultValue = "unread") String state,
                                  @RequestParam(name = "contentType", defaultValue = "all") String contentType,
                                  @RequestParam(name = "tags", required = false) List<String> tags,
                                  @RequestParam(name = "sort", defaultValue = "newest") String sort,
                                  @RequestParam(name = "limit", defaultValue = "10") int limit) {
        GetItemsRequest req;

        if (tags != null && !tags.isEmpty()) {
            req = new GetItemsRequest(userId, state, contentType, sort, limit, tags);
        } else {
            req = new GetItemsRequest(userId, state, contentType, sort, limit);
        }
        return itemServiceImpl.findByUserId(req);
    }

    @PostMapping("")
    public Item setItem(@RequestHeader("X-Later-User-Id") Long userId, @RequestParam(name = "url") String url) {
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
