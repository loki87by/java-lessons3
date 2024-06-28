package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
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
    public List<Item> findByUserId(Long userId) {
        return itemJPARepository.findItemsByUserId(userId);
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
