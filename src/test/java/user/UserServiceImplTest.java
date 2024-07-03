package user;

import org.example.config.PersistenceConfig;
import org.example.config.TestConfig;
import org.example.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, PersistenceConfig.class})
@Transactional
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    void saveUser_savesUserCorrectly() {
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        UserState state = UserState.ACTIVE;
        UserDTO userDTO = new UserDTO(firstName, lastName, email, null, state);

        User returnedUser = userService.saveUser(userDTO);

        assertThat(returnedUser, notNullValue());
        assertThat(returnedUser.getEmail(), equalTo(email));
        assertThat(returnedUser.getFirstName(), equalTo(firstName));
        assertThat(returnedUser.getLastName(), equalTo(lastName));
        assertThat(returnedUser.getState(), equalTo(state));
    }
}
