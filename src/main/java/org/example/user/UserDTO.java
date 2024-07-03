package org.example.user;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Component
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Timestamp registrationDate = Timestamp.from(Instant.now());
    private UserState state;

    public UserDTO() {
    }

    public UserDTO(String firstName, String lastName, String email, Timestamp registrationDate, UserState state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.state = state;
    }
}
