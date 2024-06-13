package org.example.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class UserInMemoryRepository implements UserRepository{
    private static final HashMap<Long, User> users = new HashMap<>();

    @Override
    public HashMap<Long, User> findAll() {
        return users;
    }

    @Override
    public boolean checkUser(Long userId) {
        User user = users.get(userId);
        return user != null;
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }
}
