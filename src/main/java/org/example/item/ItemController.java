package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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
    public ResponseEntity<List<ItemDTO>> getItems(@RequestHeader("X-Later-User-Id") Long userId,
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
        List<ItemDTO> items = itemServiceImpl.findByUserId(req);

        return ResponseEntity.ok(items);
    }

    @PostMapping("")
    public Item setItem(@RequestHeader("X-Later-User-Id") Long userId, @RequestParam(name = "url") String url) {
        Item item = new Item();
        item.setUserId(userId);
        item.setUrl(url);
        return itemServiceImpl.save(item);
    }

    @PatchMapping("")
    public Item updItem(@RequestHeader("X-Later-User-Id") Long userId,
                        @RequestParam(name = "itemId") Long itemId,
                        @RequestParam(name = "replaceTags", defaultValue = "false") boolean replaceTags,
                        @RequestParam(name = "unread", required = false) Boolean unread,
                        @RequestParam(name = "tags", required = false) List<String> tags,
                        @RequestParam(name = "url", required = false) String url) {
        ModifyItemRequest mir = new ModifyItemRequest();
        mir.setItemId(itemId);
        mir.setUserId(userId);
        mir.setReplaceTags(replaceTags);

        if(unread != null) {
            mir.setUnread(unread);
        }

        if(tags != null) {
            mir.setTags(new HashSet<>(tags));
        }

        if(url != null) {
            mir.setUrl(url);
        }
        return itemServiceImpl.update(mir);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem (@RequestHeader("X-Later-User-Id") Long userId, @PathVariable(name = "itemId") Long itemId) {
        itemServiceImpl.deleteByUserIdAndItemId(userId, itemId);
    }
}
