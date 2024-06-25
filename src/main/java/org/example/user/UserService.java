package org.example.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDTO> getAll();

    UserDTO save(User user);

    void saveUser(UserDTO userDTO);
}
