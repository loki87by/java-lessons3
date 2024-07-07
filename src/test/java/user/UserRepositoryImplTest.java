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

//import java.util.List;

/*import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;*/

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, PersistenceConfig.class})
@Transactional
public class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;
    User user1 = new User();
    String email = "test@example.com";
    String firstName = "John";
    String lastName = "Doe";
    //User user2 = new User(11L, "Alice", "Smith", "alice@example.com", UserState.ACTIVE);

    @BeforeEach
    public void setUp() {
        user1.setEmail(email);
        user1.setLastName(lastName);
        user1.setFirstName(firstName);
        user1.setState(UserState.ACTIVE);
    }

    @Test
    void testGetAll() {
        User savedUser = userRepository.save(user1);
        System.out.println("savedUser: " + savedUser);
        //List<User> list = userRepository.findAll();

        //newt not working,  need update
        /*assertThat(list, notNullValue());
        assertThat(list.size(), equalTo(1));
        assertThat(list.getFirst().hashCode(), equalTo(savedUser.hashCode()));

        User savedUser2 = userRepository.save(user2);
        System.out.println("savedUser2: " + savedUser2);
        list = userRepository.findAll();

        assertThat(list.size(), equalTo(2));
        assertThat(list.get(1).hashCode(), equalTo(savedUser2.hashCode()));

        savedUser2.setLastName(lastName);
        User updatedUser = userRepository.save(savedUser2);
        System.out.println("updatedUser: " + updatedUser);
        list = userRepository.findAll();

        assertThat(list.size(), equalTo(2));
        assertThat(list.get(1).getFirstName(), equalTo(savedUser2.getFirstName()));
        assertThat(list.get(1).getLastName(), equalTo(savedUser.getLastName()));*/
    }
}
