package org.example.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toModel(UserDTO dto) {
        //System.out.println("\u001B[38;5;44m" + "MAPPER OUTPUT--dto: " + dto + "\u001B[0m");
        User user = new User();
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
