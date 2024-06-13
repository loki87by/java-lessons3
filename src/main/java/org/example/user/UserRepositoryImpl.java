package org.example.user;

import java.util.HashMap;

public class UserRepositoryImpl implements UserRepository{
    @Override
    public HashMap<Long, User> findAll() {
        return null;
    }

    @Override
    public boolean checkUser(Long userId) {
        return false;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(Long userId) {

    }
}
