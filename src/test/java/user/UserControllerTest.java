package user;

import org.example.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserMapper userMapper;

    @InjectMocks
    private UserController controller;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<UserDTO> userList = new ArrayList<>();

        when(userService.getAll()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(userList.size())));

        verify(userService, times(1)).getAll();
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = new User();
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setState(UserState.ACTIVE);
        System.out.println("user: "+user);
        UserDTO savedUser = userMapper.toObj(user);
        System.out.println("savedUser: "+savedUser);

        when(userService.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{ \"firstName\": \"John\",  \"lastName\": \"Doe\", \"email\": \"john@example.com\" }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).save(any(User.class));
    }
}