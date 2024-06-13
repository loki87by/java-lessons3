package org.example.user;

import org.example.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    UserInMemoryRepository userInMemoryRepository;
    Utils utils;
    UserMapper userMapper;

    @Autowired
    public UserService(UserInMemoryRepository userInMemoryRepository, Utils utils, UserMapper userMapper) {
        this.userInMemoryRepository = userInMemoryRepository;
        this.utils = utils;
        this.userMapper = userMapper;
    }

    public List<UserDTO> findAll() {
        HashMap<Long, User> users = userInMemoryRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();

        for (User user : users.values()) {
            dtos.add(new UserMapper().toDTO(user));
        }
        return dtos;
    }

    public User save(UserDTO dto) {
        HashMap<Long, User> users = userInMemoryRepository.findAll();
        Long hash = Math.abs(Long.parseLong(String.valueOf(users.hashCode())));
        Long id = utils.getUniqueId(users, hash);
        User user = userMapper.toModel(dto, id);
        userInMemoryRepository.save(user);
        return user;
    }

    public UserDTO update(UserDTO dto, Long userId) {
        User user = userMapper.toModel(dto, userId);
        userInMemoryRepository.save(user);
        return dto;
    }

    public String delete(Long userId) {
        int length = userInMemoryRepository.findAll().size();
        userInMemoryRepository.delete(userId);
        int newLength = userInMemoryRepository.findAll().size();

        if (length > newLength) {
            return "success";
        } else {
            return "error: user not found";
        }
    }
}
