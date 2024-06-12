package org.example.user;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Instant registrationDate = Instant.now();
    private UserState state;
}
