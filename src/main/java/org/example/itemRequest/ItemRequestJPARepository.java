package org.example.itemRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ItemRequestJPARepository extends JpaRepository<ItemRequest, Long> {
    @Override
    List<ItemRequest> findAll();
    List<ItemRequest> findByOwnerId(Long id);
    Optional<ItemRequest> findById(Long id);
}
