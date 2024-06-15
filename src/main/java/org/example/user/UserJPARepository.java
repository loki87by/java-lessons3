package org.example.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserJPARepository extends JpaRepository<User, Long> {
    int countByEmail(String email);
    int countById(Long id);
    User findUsersById(Long id);
}
