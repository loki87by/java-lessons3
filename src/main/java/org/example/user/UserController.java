package org.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;
    Long max = Long.MAX_VALUE;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<UserDTO> getAll() {
        return userService.findAll();
    }

    @PostMapping("")
    public User save(@RequestBody UserDTO dto) {
        return userService.save(dto);
    }

    @PatchMapping("")
    public UserDTO update(@RequestHeader(value = "X-Later-User-Id", defaultValue = "" + Long.MAX_VALUE) Long userId,
                          @RequestBody UserDTO dto) {

        if (!Objects.equals(userId, max)) {
            return userService.update(dto, userId);
        }
        throw new SecurityException("недостаточно прав для операции.");
    }

    @DeleteMapping("")
    public String delete(@RequestHeader(value = "X-Later-User-Id", defaultValue = "" + Long.MAX_VALUE) Long userId) {

        if (!Objects.equals(userId, max)) {
            return userService.delete(userId);
        }
        throw new SecurityException("недостаточно прав для операции.");
    }
}
