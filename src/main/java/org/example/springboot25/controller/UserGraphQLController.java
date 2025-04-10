package org.example.springboot25.controller;

import org.example.springboot25.DTO.UserInputDTO;
import org.example.springboot25.DTO.UserOutputDTO;
import org.example.springboot25.DTO.UserUpdateDTO;
import org.example.springboot25.Mapper.UserMapper;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserGraphQLController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserGraphQLController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @QueryMapping
    public List<UserOutputDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(userMapper::toDto).toList();
    }

    @QueryMapping
    public UserOutputDTO getUserById(@Argument Long userId) {
        User user = userService.getUserById(userId);
        if(user == null) {
            throw new RuntimeException("User with id " + userId + " not found");
        }
        return userMapper.toDto(user);
    }

    @QueryMapping
    public UserOutputDTO getUserByUserName(@Argument String userName) {
        User user = userService.getUserByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User with name " + userName + " not found");
        }
        return userMapper.toDto(user);
    }

    @MutationMapping
    public UserOutputDTO createUser(@Argument("input") UserInputDTO input) {
        validateUserInput(input);
        User user = userMapper.toUser(input);
        return userMapper.toDto(userService.addUser(user));
    }

    @MutationMapping
    public UserOutputDTO updateUser(@Argument Long userId, @Argument("input") UserUpdateDTO input) {
        if(input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        User userToUpdate = userService.getUserById(userId);
        if(userToUpdate == null) {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
        userMapper.updateUserFromDto(input, userToUpdate);
        return userMapper.toDto(userService.updateUser(userId, userToUpdate));
    }

    @MutationMapping
    public boolean deleteUser(@Argument Long userId) {

        try {
            userService.deleteUserById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void validateUserInput(UserInputDTO input) {
       if (input == null) {
           throw new IllegalArgumentException("User input cannot be null");
       }
       if (input.getUserName() == null || input.getUserName().trim().isEmpty()) {
           throw new IllegalArgumentException("Username is required");
       }
       if (input.getUserEmail() == null || input.getUserEmail().trim().isEmpty()) {
           throw new IllegalArgumentException("Email is required");
       }
     }

}
