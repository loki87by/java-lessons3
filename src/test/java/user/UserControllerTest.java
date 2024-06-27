package user;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserMapper userMapper;

    @InjectMocks
    private UserController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private UserDTO userDTO;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        userDTO = new UserDTO();
        userDTO.setState(UserState.ACTIVE);
        userDTO.setEmail("jonh.doe@mail.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setRegistrationDate(Timestamp.from(Instant.now()));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<UserDTO> userList = new ArrayList<>();

        when(userService.getAll()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void saveNewUserWithException() throws Exception {
        when(userService.save(any()))
                .thenThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(mapper.writeValueAsString(userDTO))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(500));
    }
}