package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ItemServiceImpl {

    private final ItemMapper itemMapper;
    private final UrlMetadataRetriever urlMetadataRetriever;
    private final ItemJPARepository itemJPARepository;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper,
                           ItemJPARepository itemJPARepository,
                           @Qualifier("urlMetadataRetrieverImpl") UrlMetadataRetriever urlMetadataRetriever) {
        this.itemMapper = itemMapper;
        this.urlMetadataRetriever = urlMetadataRetriever;
        this.itemJPARepository = itemJPARepository;
    }

    @Transactional
    public List<ItemDTO> findByUserId(GetItemsRequest req) {
        Boolean state;

        if (req.getState() == LINK_STATE.READ) {
            state = false;
        } else if (req.getState() == LINK_STATE.UNREAD) {
            state = true;
        } else {
            state = null;
        }
        String contentType;

        if (req.getContentType() == LINK_CONTENT_TYPE.ARTICLE) {
            contentType = "text";
        } else if (req.getContentType() == LINK_CONTENT_TYPE.IMAGE) {
            contentType = "image";
        } else if (req.getContentType() == LINK_CONTENT_TYPE.VIDEO) {
            contentType = "video";
        } else {
            contentType = null;
        }
        Set<String> tg = null;

        if(!req.getTags().isEmpty()) {
            tg = new HashSet<>(req.getTags());
        }
        List<Item> items = itemJPARepository.findItemsByUserIdAndUnreadAndMimeTypeContainingAndTags(req.getUserId(),
                state,
                contentType,
                tg);

        if (req.getSort() != null) {
            switch (req.getSort()) {
                case OLDEST:
                    items.sort(Comparator.comparing(Item::getDateResolved));
                    break;
                case TITLE:
                    items.sort(Comparator.comparing(Item::getTitle));
                    break;
                default:
                    items.sort(Comparator.comparing(Item::getDateResolved).reversed());
            }
        }

        //System.out.println("\u001B[38;5;44m" + "items: "+items+ "\u001B[0m");
        List<ItemDTO> result = new ArrayList<>();
        int minSize = Math.min(items.size(), req.getLimit());
        for (int i = 0; i < minSize; i++) {
            result.add(itemMapper.toObj(items.get(i)));
        }

        return result;
    }

    @Transactional
    public Item save(Item item) {
        UrlMetadataRetriever.UrlMetadata meta = urlMetadataRetriever.retrieve(item.getUrl());
        String resUrl = meta.getResolvedUrl();
        boolean hasDuplicates = itemJPARepository.countItemsByResolvedUrl(resUrl) > 0;

        if (hasDuplicates) {
            Item oldItem = itemJPARepository.findItemByResolvedUrl(resUrl);
            Item updItem = new ItemMapper().addMetadata(oldItem, meta);
            return itemJPARepository.saveAndFlush(updItem);
        }
        Item updItem = new ItemMapper().addMetadata(item, meta);
        return itemJPARepository.saveAndFlush(updItem);
    }

    /*public void deleteByUserIdAndItemId(long userId, long itemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == itemId && items.get(i).getUserId() == userId) {
                items.remove(i);
                break;
            }
        }
    }*/

}
