package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserGraphQLController {

    private final UserService userService;

    public UserGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    @Cacheable(value = "users")
    public List<UserOutputDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @QueryMapping
    @Cacheable(value = "users", key = "#userId")
    public UserOutputDTO getUserById(@Argument Long userId) {
        return userService.getUserDtoById(userId);
    }

    @QueryMapping
    @Cacheable(value = "usersByUsername", key = "#userName")
    public UserOutputDTO getUserByUserName(@Argument String userName) {
        return userService.getUserDtoByUserName(userName);
    }

    @MutationMapping
    public UserOutputDTO createUser(@Argument("input") @Valid UserInputDTO input) {
        return userService.addUser(input);
    }

    @MutationMapping
    @CachePut(value = "users", key = "#userId")
    public UserOutputDTO updateUser(@Argument Long userId, @Argument("input") UserUpdateDTO input) {
        return userService.updateUser(userId, input);
    }

    @MutationMapping
    @CacheEvict(value = {"users", "usersByUsername", "usersByEmail"}, key = "#userId")
    public boolean deleteUser(@Argument Long userId) {
        try {
            userService.deleteUserById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
