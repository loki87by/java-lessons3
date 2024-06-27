package org.example.user;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Timestamp registrationDate = Timestamp.from(Instant.now());
    private UserState state;
}
