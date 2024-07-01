package org.example.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface ItemJPARepository extends JpaRepository<Item, Long> {
    int countItemsByResolvedUrl(String uri);

    Item findItemByResolvedUrl(String uri);

    List<Item> findItemsByUserId(Long userId);

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
                return findItemsByUserIdAndUnreadAndMimeTypeContainingAndTags(userId, unread, mimeType, tags);
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
}
