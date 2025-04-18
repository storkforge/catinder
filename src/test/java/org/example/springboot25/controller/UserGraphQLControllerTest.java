package org.example.springboot25.controller;

import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGraphQLControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserGraphQLController userGraphQLController;

    @Test
    @DisplayName("Should fetch all users")
    void shouldFetchAllUsers() {
        UserOutputDTO userOutputDTO = new UserOutputDTO(
                1L, "Name", "@user", "mail@test.com",
                "City", UserRole.BASIC, "LOCAL"
        );

        when(userService.getAllUsers()).thenReturn(List.of(userOutputDTO));

        List<UserOutputDTO> result = userGraphQLController.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("@user", result.get(0).getUserName());
    }

    @Test
    @DisplayName("Should fetch user by ID")
    void shouldFetchUserById() {
        UserOutputDTO userOutputDTO = new UserOutputDTO(1L, "Name",
                "@user", "mail@test.com", "City", UserRole.BASIC, "LOCAL");

        when(userService.getUserDtoById(1L)).thenReturn(userOutputDTO);
        UserOutputDTO result = userGraphQLController.getUserById(1L);
        assertEquals(1L, result.getUserId());
    }

    @Test
    @DisplayName("Should fetch user by username")
    void shouldFetchUserByUsername() {
        UserOutputDTO userOutputDTO = new UserOutputDTO(1L, "Name",
                "@user", "mail@test.com", "City", UserRole.BASIC, "LOCAL");

        when(userService.getUserDtoByUserName("@user")).thenReturn(userOutputDTO);
        UserOutputDTO result = userGraphQLController.getUserByUserName("@user");
        assertEquals("@user" , result.getUserName());
    }

    @Test
    @DisplayName("Should create a new user")
    void shouldCreateNewUser() {
        UserInputDTO userInputDTO = new UserInputDTO("Name",
                "@user", "mail@test.com", "City", UserRole.BASIC, "LOCAL");
        UserOutputDTO userOutputDTO = new UserOutputDTO(1L, "Name",
                "@user", "mail@test.com", "City", UserRole.BASIC, "LOCAL");
        when(userService.addUser(userInputDTO)).thenReturn(userOutputDTO);

        UserOutputDTO result = userGraphQLController.createUser(userInputDTO);
        assertEquals("@user", result.getUserName());
    }

    @Test
    @DisplayName("Should update an existing user")
    void shouldUpdateAnExistingUser() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Updated Name",
                null, null, null, null, null);
        UserOutputDTO userOutputDTO = new UserOutputDTO(1L, "Updated Name",
                "@user", "mail@test.com", "City", UserRole.BASIC, "LOCAL");

        when(userService.updateUser(1L, userUpdateDTO)).thenReturn(userOutputDTO);
        UserOutputDTO result = userGraphQLController.updateUser(1L, userUpdateDTO);
        assertEquals("Updated Name" , result.getUserFullName());
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        doNothing().when(userService).deleteUserById(1L);
        boolean result = userGraphQLController.deleteUser(1L);

        assertTrue(result);
        verify(userService).deleteUserById(1L);
    }

    @Test
    @DisplayName("Should return false if delete fails")
    void shouldReturnFalseIfDeleteFails() {
        doThrow(new RuntimeException("fail")).when(userService).deleteUserById(1L);
        boolean result = userGraphQLController.deleteUser(1L);
        assertFalse(result);
    }
}
