package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserGraphQLController {

    private final UserService userService;

    public UserGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserOutputDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public UserOutputDTO getUserById(@Argument Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());
        userService.checkIfOwnerOrAdmin(userId, currentUser);
        return userService.getUserDtoById(userId);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public UserOutputDTO getUserByUserName(@Argument String userName) {
        return userService.getUserDtoByUserName(userName);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public UserOutputDTO updateUser(@Argument Long userId, @Argument("input") @Valid UserUpdateDTO input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());
        userService.checkIfOwnerOrAdmin(userId, currentUser);
        return userService.updateUser(userId, input);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public boolean deleteUser(@Argument Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());
        userService.checkIfOwnerOrAdmin(userId, currentUser);
        userService.deleteUserById(userId);
        return true;
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public UserOutputDTO createUser(@Argument("input") @Valid UserInputDTO input) {
        return userService.addUser(input);
    }
}
