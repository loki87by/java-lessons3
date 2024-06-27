package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.example.user.UserMapper;

@Configuration
public class TestConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }
}

