package org.example.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class UserRepository {
    private static final HashMap<Long, User> users = new HashMap<>();

    public HashMap<Long, User> findAll() {
        return users;
    }

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public void delete(Long userId) {
        users.remove(userId);
    }
}
