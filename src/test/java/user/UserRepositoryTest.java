package user;

import org.example.config.PersistenceConfig;
import org.example.config.TestConfig;
import org.example.user.User;
import org.example.user.UserRepositoryImpl;
import org.example.user.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, PersistenceConfig.class})
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepositoryImpl userRepository;
    User user1 = new User();
    String email = "test@example.com";
    String firstName = "John";
    String lastName = "Doe";
    User user2 = new User(11L, "Alice", "Smith", "alice@example.com", UserState.ACTIVE);

    @BeforeEach
    public void setUp() {
        user1.setEmail(email);
        user1.setLastName(lastName);
        user1.setFirstName(firstName);
        user1.setState(UserState.ACTIVE);
    }

    @Test
    void setUserTest() {
        User savedUser = userRepository.save(user1);

        assertThat(savedUser, notNullValue());
        assertThat(savedUser.getRegistrationDate(), notNullValue());
        assertThat(savedUser.getId(), equalTo(user1.getId()));
        assertThat(savedUser.getState(), equalTo(user1.getState()));
        assertThat(savedUser.getLastName(), equalTo(user1.getLastName()));
        assertThat(savedUser.getEmail(), equalTo(user1.getEmail()));
        assertThat(savedUser.getFirstName(), equalTo(user1.getFirstName()));

        savedUser = userRepository.save(user2);

        assertThat(savedUser, notNullValue());
        assertThat(savedUser.getRegistrationDate(), notNullValue());
        assertThat(savedUser.getId(), equalTo(user2.getId()));
        assertThat(savedUser.getState(), equalTo(user2.getState()));
        assertThat(savedUser.getLastName(), equalTo(user2.getLastName()));
        assertThat(savedUser.getEmail(), equalTo(user2.getEmail()));
        assertThat(savedUser.getFirstName(), equalTo(user2.getFirstName()));

        user2.setFirstName(firstName);
        user2.setEmail(email);
        user2.setFirstName(firstName);
        user2.setLastName(lastName);
        savedUser = userRepository.save(user2);

        assertThat(savedUser, notNullValue());
        assertThat(savedUser.getRegistrationDate(), notNullValue());
        assertThat(savedUser.getId(), equalTo(user2.getId()));
        assertThat(savedUser.getLastName(), equalTo(user1.getLastName()));
        assertThat(savedUser.getEmail(), equalTo(user1.getEmail()));
        assertThat(savedUser.getFirstName(), equalTo(user1.getFirstName()));
    }
}
