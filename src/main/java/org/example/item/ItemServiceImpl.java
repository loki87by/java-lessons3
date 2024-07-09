package org.example.item;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ItemServiceImpl {

    //private final ItemMapper itemMapper;
    private final UrlMetadataRetriever urlMetadataRetriever;
    private final ItemJPARepository itemJPARepository;

    @Autowired
    public ItemServiceImpl(/*ItemMapper itemMapper,*/
                           ItemJPARepository itemJPARepository,
                           @Qualifier("urlMetadataRetrieverImpl") UrlMetadataRetriever urlMetadataRetriever) {
        //this.itemMapper = itemMapper;
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
        items.forEach(item -> Hibernate.initialize(item.getTags()));

        //System.out.println("\u001B[38;5;44m" + "items: "+items+ "\u001B[0m");
        List<ItemDTO> result = new ArrayList<>();
        int minSize = Math.min(items.size(), req.getLimit());
        for (int i = 0; i < minSize; i++) {
            result.add(new ItemDTO(items.get(i).getId(),
                    items.get(i).getUrl(),
                    items.get(i).getTags(),
                    items.get(i).getResolvedUrl(),
                    items.get(i).getMimeType(),
                    items.get(i).getTitle(),
                    items.get(i).isHasImage(),
                    items.get(i).isHasVideo(),
                    items.get(i).getDateResolved(),
                    items.get(i).isUnread()));
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

    private Set<String> updateTags(Long itemId, Set<String> tags, boolean isReplaceTags) {

        if (!isReplaceTags) {
            return itemJPARepository.saveUniqueTagsForItem(itemId, tags);
        } else {
            return itemJPARepository.replaceTagsForItem(itemId, tags);
        }
    }

    private Item getItemByIds (Long userId, Long itemId) {
        List<Item> uItems = itemJPARepository.findItemsByUserId(userId);
        return uItems.stream().filter(item -> Objects.equals(item.getId(), itemId)).findFirst().orElse(null);
    }

    @Transactional
    public Item update(ModifyItemRequest req) {
        Item item = getItemByIds(req.getUserId(), req.getItemId());

        if (item == null) {
            throw new IllegalArgumentException();
        }
        Set<String> newTags = updateTags(req.getItemId(), req.getTags(), req.isReplaceTags());
        //String url = item.getUrl();

        final Item updatedItem;
        if(req.getUrl() != null && !req.getUrl().equals(item.getUrl())) {
        //if(req.getUrl() != null && !req.getUrl().equals(url)) {
            //item.setUrl(url);
            item.setUrl(req.getUrl());
            updatedItem = save(item);
        } else {
            updatedItem = item;
        }

        if(req.getUnread() != null) {
            updatedItem.setUnread(req.getUnread());
        }
        updatedItem.setTags(newTags);
        return updatedItem;
    }

    @Transactional
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        Item item = getItemByIds(userId, itemId);

        if (item != null) {
            itemJPARepository.deleteItemById(itemId);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
