package org.example.item;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final CommentJPARepository commentJPARepository;
    @Autowired
    public CommentRepositoryImpl(CommentJPARepository commentJPARepository) {
        this.commentJPARepository = commentJPARepository;
    }

    @Override
    @Transactional
    public String setComment(Long itemId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setDescription(content);
        comment.setItemId(itemId);
        comment.setOwnerId(userId);
        commentJPARepository.saveAndFlush(comment);
        return "Комментарий сохранен.";
    }
}
