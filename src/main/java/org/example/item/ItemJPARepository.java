package org.example.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ItemJPARepository extends JpaRepository<Item, Long> {
    int countItemsByResolvedUrl(String uri);
    Item findItemByResolvedUrl(String uri);
    List<Item> findItemsByUserId(Long id);
    List<Item> findItemWithTagsByUserId(Long id);
    //Item updateItemByResolvedUrl(String url, Item item);

}
