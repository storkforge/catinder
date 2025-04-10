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
        return userMapper.toDto(userService.getUserById(userId));
    }

    @QueryMapping
    public UserOutputDTO getUserByUserName(@Argument String userName) {
        return userMapper.toDto(userService.getUserByUserName(userName));
    }



    @MutationMapping
    public UserOutputDTO createUser(@Argument("input") UserInputDTO input) {
        User user = userMapper.toUser(input);
        return userMapper.toDto(userService.addUser(user));
    }

    @MutationMapping
    public UserOutputDTO updateUser(@Argument Long userId, @Argument("input") UserUpdateDTO input) {
        User userToUpdate = userService.getUserById(userId);
        userMapper.updateUserFromDto(input, userToUpdate);
        return userMapper.toDto(userService.updateUser(userId, userToUpdate));
    }

    @MutationMapping
    public boolean deleteUser(@Argument Long userId) {
        userService.deleteUserById(userId);
        return true;
    }
}
