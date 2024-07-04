package org.example.user;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@Table(name = "users", schema = "public") //schema isn`t required in postgre
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false) //unique(bool) for set originals
    private String firstName;
    @Column(name = "last_name", nullable = false) //length=10 - is equal of varchar(10)
    private String lastName;
    private String email; //@Transient for not saving in DB
    @Column(name = "registration_date")
    private Timestamp registrationDate = Timestamp.from(Instant.now());
    @Enumerated(EnumType.STRING) //EnumType.ORDINAL - for index from enum
    private UserState state;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, UserState state) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.state = state;
    }
}
