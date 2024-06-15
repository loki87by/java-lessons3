package org.example.user;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.HashMap;

@RepositoryRestResource
public interface UserRepository {
    HashMap<Long, User> findAll();

    boolean checkUser(Long userId);

    void save(User user);

    void delete(Long userId);
}
