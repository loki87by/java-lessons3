package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.example.user.UserMapper;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistenceConfig.class)
public class TestConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }
}

