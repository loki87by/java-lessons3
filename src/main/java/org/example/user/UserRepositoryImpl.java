package org.example.user;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final EntityManager entityManager;
    private final UserJPARepository userJPARepository;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager, UserJPARepository userJPARepository) {
        this.entityManager = entityManager;
        this.userJPARepository = userJPARepository;
    }

    @Override
    @Transactional
    public void save(User user) {
        boolean isUniqueUserEmail = userJPARepository.countByEmail(user.getEmail()) != 0;

        if (user.getId() != null) {

            if (userJPARepository.countById(user.getId()) == 1) {
                User current = userJPARepository.findUsersById(user.getId());

                if(!current.getEmail().equals(user.getEmail()) && isUniqueUserEmail) {
                    throw new IllegalArgumentException("Пользователь с таким email уже существует.");
                }
                entityManager.merge(user);
            } else {
                throw new NoSuchElementException("Отсутствует пользователь с id="+user.getId());
            }
        } else {

            if (isUniqueUserEmail) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует.");
            } else {
                entityManager.persist(user);
            }
        }
    }

    @Override
    public HashMap<Long, User> findAll() {
        List<User> users = userJPARepository.findAll();
        HashMap<Long, User> mapa = new HashMap<>();
        for (User user : users) {
            mapa.put(user.getId(), user);
        }
        return mapa;
    }

    @Override
    public boolean checkUser(Long userId) {
        return userJPARepository.findUsersById(userId) != null;
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User user = userJPARepository.findUsersById(userId);
        //System.out.println("\u001B[38;5;44m" + "user: "+user+ "\u001B[0m");
        if (user != null) {
            entityManager.remove(user);
        } else {
            throw new NoSuchElementException("В БД нет пользователя с id="+userId);
        }
    }
}
