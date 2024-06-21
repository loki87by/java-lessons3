package org.example.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CommentJPARepository extends JpaRepository<Comment, Long> {
    List<CommentDTO> findAllByItemId(Long itemId);
}
