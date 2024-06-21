package org.example.item;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CommentRepository {
    String setComment(Long itemId, Long userId, String content);
}
