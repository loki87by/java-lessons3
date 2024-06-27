package org.example.config;

import org.example.user.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.persistence.EntityManager;

@Configuration
public class ServiceConfig {

    @Bean
    public UserService userService(UserRepositoryImpl userRepositoryImpl, UserMapper userMapper) {
        return new UserServiceImpl(userRepositoryImpl, userMapper);
    }

    @Bean
    public UserRepositoryImpl userRepositoryImpl(EntityManager entityManager, UserJPARepository userJPARepository) {
        return new UserRepositoryImpl(entityManager, userJPARepository);
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }
}
