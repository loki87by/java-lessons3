package org.example.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toModel (UserDTO dto/*, Long id*/) {
        User user = new User();
        //user.setId(id);
        user.setEmail(dto.getEmail());
        user.setState(dto.getState());
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setRegistrationDate(dto.getRegistrationDate());
        return user;
    }

    public UserDTO toObj (User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setState(user.getState());
        dto.setRegistrationDate(user.getRegistrationDate());
        return dto;
    }
}
