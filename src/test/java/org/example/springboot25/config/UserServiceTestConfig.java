package org.example.springboot25.config;

import org.example.springboot25.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserServiceTestConfig {

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

}
