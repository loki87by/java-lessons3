package org.example.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface ItemJPARepository extends JpaRepository<Item, Long> {
    int countItemsByResolvedUrl(String uri);

    Item findItemByResolvedUrl(String uri);

    List<Item> findItemsByUserId(Long userId);

    void deleteItemById(Long id);

    List<Item> findItemsByUserIdAndUnread(Long userId, Boolean state);

    List<Item> findItemsByUserIdAndTags(Long userId, Set<String> tags);

    List<Item> findItemsByUserIdAndMimeTypeContaining(Long userId, String mimeType);

    List<Item> findItemsByUserIdAndUnreadAndTags(Long userId, Boolean unread, Set<String> tags);

    List<Item> findItemsByUserIdAndMimeTypeContainingAndTags(Long userId, String mimeType, Set<String> tags);

    List<Item> findItemsByUserIdAndUnreadAndMimeTypeContaining(Long userId,
                                                               Boolean state,
                                                               String contentType);

    default List<Item> findItemsByUserIdAndUnreadAndMimeTypeContainingAndTags(Long userId,
                                                                              Boolean unread,
                                                                              String mimeType,
                                                                              Set<String> tags) {

        if (unread == null && (mimeType == null || mimeType.isEmpty()) && (tags == null || tags.isEmpty())) {
            return findItemsByUserId(userId);
        }

        if (unread != null) {

            if (mimeType != null && !mimeType.isEmpty() && tags != null && !tags.isEmpty()) {
                return findItemsByUserIdAndMimeTypeContainingAndTags(userId, mimeType, tags);
            } else if (mimeType != null && !mimeType.isEmpty()) {
                return findItemsByUserIdAndUnreadAndMimeTypeContaining(userId, unread, mimeType);
            } else if (tags != null && !tags.isEmpty()) {
                return findItemsByUserIdAndUnreadAndTags(userId, unread, tags);
            } else {
                return findItemsByUserIdAndUnread(userId, unread);
            }

        } else {
            if (mimeType != null && !mimeType.isEmpty() && tags != null && !tags.isEmpty()) {
                return findItemsByUserIdAndMimeTypeContainingAndTags(userId, mimeType, tags);
            } else if (mimeType != null && !mimeType.isEmpty()) {
                return findItemsByUserIdAndMimeTypeContaining(userId, mimeType);
            } else {
                return findItemsByUserIdAndTags(userId, tags);
            }
        }
    }

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tags (item_id, name) VALUES (:itemId, :tagName)", nativeQuery = true)
    void saveTagForItem(Long itemId, String tagName);

    @Query(value = "SELECT t.name FROM tags t WHERE t.item_id = :itemId", nativeQuery = true)
    Set<String> getTagsForItem(@Param("itemId") Long itemId);

    default Set<String> saveUniqueTagsForItem(Long itemId, Set<String> tags) {

        if (tags != null) {
            Set<String> existingTags = getTagsForItem(itemId);
            tags.stream()
                    .filter(tag -> !existingTags.contains(tag))
                    .forEach(tag -> saveTagForItem(itemId, tag));

            return getTagsForItem(itemId);
        } else {
            return Collections.emptySet();
        }
    }

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tags t WHERE t.item_id = :itemId", nativeQuery = true)
    void deleteTagsForItem(Long itemId);

    @Modifying
    @Transactional
    default Set<String> replaceTagsForItem(Long itemId, Set<String> tags) {
        deleteTagsForItem(itemId);
        tags.forEach(tag -> saveTagForItem(itemId, tag));

        return tags;
    }


}
