package org.example.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl {
    private final List<Item> items = new ArrayList<>();
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

    public List<ItemDTO> findByUserId(long userId) {
        return items.stream().filter(item -> item.getUserId() == userId).toList()
                .stream()
                .map(itemMapper::toObj)
                .collect(Collectors.toList());
    }

    public Item save(Item item) {
        UrlMetadataRetriever.UrlMetadata meta = urlMetadataRetriever.retrieve(item.getUrl());
        System.out.println("meta: "+meta);
        Item updItem = new ItemMapper().addMetadata(item, meta);
        System.out.println("updItem: "+updItem);
        return itemJPARepository.saveAndFlush(updItem);
        //System.out.println("resultItem: "+resultItem);
        //entityManager.persist(updItem);
        //return itemMapper.toObj(resultItem);
    }

    public void deleteByUserIdAndItemId(long userId, long itemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == itemId && items.get(i).getUserId() == userId) {
                items.remove(i);
                break;
            }
        }
    }

}
