package org.example.user;

import java.util.HashMap;

public interface UserRepository {
    HashMap<Long, User> findAll();

    boolean checkUser(Long userId);

    void save(User user);

    void delete(Long userId);
}
