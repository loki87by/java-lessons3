package org.example.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    UserRepository userRepository = new UserRepository();
    @GetMapping("/users")
    public List<User> getAll() {
        return userRepository.findAll();
    }
    @PostMapping("/users")
    public User save(@RequestBody User user) {
        return userRepository.save(user);
    }
}
