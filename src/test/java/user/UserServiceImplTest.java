package user;

import org.example.config.PersistenceConfig;
import org.example.config.TestConfig;
import org.example.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, PersistenceConfig.class})
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserService userService;


    @MockBean
    private UserRepositoryImpl userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getUsers() {
        User user1 = new User(1L, "John", "Doe", "john@example.com", UserState.ACTIVE);
        User user2 = new User(2L, "Alice", "Smith", "alice@example.com", UserState.ACTIVE);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> userDTOList = userService.getAll();

        Mockito.verify(userRepository, Mockito.times(1)).findAll();

        assertThat(userDTOList, notNullValue());
        assertThat(userDTOList.size(), equalTo(2));
        assertThat(user1.getEmail(), equalTo(userDTOList.getFirst().getEmail()));
    }
}
