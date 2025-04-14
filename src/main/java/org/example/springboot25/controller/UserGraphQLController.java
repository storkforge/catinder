package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.UserMapper;
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

    public UserGraphQLController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
    }

    @QueryMapping
    public List<UserOutputDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @QueryMapping
    public UserOutputDTO getUserById(@Argument Long userId) {
        return userService.getUserById(userId);
    }

    @QueryMapping
    public UserOutputDTO getUserByUserName(@Argument String userName) {
        return userService.getUserDtoByUserName(userName);
    }

    @MutationMapping
    public UserOutputDTO createUser(@Argument("input") @Valid UserInputDTO input) {
        return userService.addUser(input);
    }

    @MutationMapping
    public UserOutputDTO updateUser(@Argument Long userId, @Argument("input") UserUpdateDTO input) {
        return userService.updateUser(userId, input);
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
       if (input.getUserFullName() == null || input.getUserFullName().trim().isEmpty()) {
           throw new IllegalArgumentException("User full name cannot be null or empty");
       }
       if (input.getUserName() == null || input.getUserName().trim().isEmpty()) {
           throw new IllegalArgumentException("Username is required");
       }
       if (input.getUserEmail() == null || input.getUserEmail().trim().isEmpty()) {
           throw new IllegalArgumentException("Email is required");
       }
       if(input.getUserLocation() == null || input.getUserLocation().trim().isEmpty()) {
           throw new IllegalArgumentException("Location is required");
       }
       if(input.getUserRole() == null) {
           throw new IllegalArgumentException("Role is required");
       }
       if(input.getUserAuthProvider() == null || input.getUserAuthProvider().trim().isEmpty()) {
           throw new IllegalArgumentException("Auth provider is required");
       }
     }


}
