package org.example.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ItemJPARepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerIdIs(Long userId);
    Optional<Item> findById(Long id);

    @Override
    List<Item> findAll();
    List<Item> findItemsByOwnerIdAndBookedIsTrue(Long id);
}
