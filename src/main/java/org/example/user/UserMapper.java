package org.example.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDTO toDTO (User user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public User toModel (UserDTO dto, Long id) {
        User user = new User();
        user.setName(dto.getName());
        user.setId(id);
        user.setEmail(dto.getEmail());
        return user;
    }
}
