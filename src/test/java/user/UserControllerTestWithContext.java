package user;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.config.*;
import org.example.user.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringJUnitWebConfig({ UserController.class, TestConfig.class, WebConfig.class, ServiceConfig.class })
public class UserControllerTestWithContext {

    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService userService;
    private MockMvc mvc;
    private UserDTO userDTO;


    @Autowired
    UserControllerTestWithContext(UserService userService) {
        this.userService = userService;
    }
    @BeforeEach
    public void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
    .build();

        userDTO = new UserDTO();
        userDTO.setState(UserState.ACTIVE);
        userDTO.setEmail("jonh.doe@mail.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setRegistrationDate(Timestamp.from(Instant.now()));
    }

    @Test
    void saveNewUser() throws Exception {
        when(userService.save(any(User.class)))
                .thenReturn(userDTO);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDTO))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(500));
    }
}