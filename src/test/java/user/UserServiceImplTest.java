package user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.example.config.PersistenceConfig;
import org.example.config.TestConfig;
import org.example.user.*;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Rollback(false)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringJUnitConfig( {PersistenceConfig.class, UserServiceImpl.class})
@ContextConfiguration(classes = TestConfig.class)
class UserServiceImplTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private UserService service;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Transactional
    void saveUser() {
        MockitoAnnotations.initMocks(this);
        transactionManager = new JpaTransactionManager(entityManagerFactory);
        UserMapper mockedUserMapper = Mockito.mock(UserMapper.class);
        UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl(entityManager);
        service = new UserServiceImpl(userRepositoryImpl, mockedUserMapper);
        UserDTO userDTO = makeUserDto("some@email.com", "Пётр", "Иванов");
        service.saveUser(userDTO);

        TypedQuery<User> query = entityManager.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDTO.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getFirstName(), equalTo(userDTO.getFirstName()));
        assertThat(user.getLastName(), equalTo(userDTO.getLastName()));
        assertThat(user.getEmail(), equalTo(userDTO.getEmail()));
        assertThat(user.getState(), equalTo(userDTO.getState()));
        assertThat(user.getRegistrationDate(), equalTo(userDTO.getRegistrationDate()));
    }

    private UserDTO makeUserDto (String email, String firstName, String lastName) {
        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setRegistrationDate(Instant.now());
        dto.setState(UserState.ACTIVE);
        return dto;
    }
}
