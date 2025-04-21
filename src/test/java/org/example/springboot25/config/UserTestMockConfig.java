package org.example.springboot25.config;

import org.example.springboot25.mapper.UserMapper;
import org.example.springboot25.security.CustomOAuth2UserService;
import org.example.springboot25.security.CustomUserDetailsService;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserTestMockConfig {

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public CatService catService() {
        return Mockito.mock(CatService.class);
    }

    @Bean
    public UserMapper userMapper() {
        return Mockito.mock(UserMapper.class);
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return Mockito.mock(CustomOAuth2UserService.class);
    }
}
