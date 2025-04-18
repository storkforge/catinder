package org.example.springboot25.service;

import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.exceptions.AlreadyExistsException;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.UserMapper;
import org.example.springboot25.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.example.springboot25.entities.UserRole.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return user when id exists")
    void shouldUserWhenIdExists() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(user.getUserId());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findUserById(userId);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Should throw exception when user id not found")
    void shouldThrowExceptionWhenUserIdNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findUserById(userId));
    }

    @Test
    @DisplayName("Should return user dto when id exist")
    void shouldUserDtoWhenIdExists() {
        Long userId = 1L;
        User user = new User();
        UserOutputDTO outputDTO = new UserOutputDTO(1L, "Test Name",
                "Username", "mail@test.com", "City", BASIC, "LOCAL");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(outputDTO);

        UserOutputDTO result = userService.getUserDtoById(userId);
        assertEquals(outputDTO, result);
    }

    @Test
    @DisplayName("Should add user from dto")
    void shouldAddUserFromDto() {
        String name = "Test Name";
        String username = "Username";
        String mail = "mail@test.com";
        String location = "City";
        UserRole userRole = BASIC;
        String authprovider = "LOCAL";

        UserInputDTO inputDTO = new UserInputDTO(name, username, mail, location, userRole, authprovider);
        User user = new User();
        User savedUser = new User();
        savedUser.setUserId(1L);
        UserOutputDTO outputDTO = new UserOutputDTO(1L, name, username, mail, location, userRole, authprovider);

        when(userRepository.existsByUserEmail(mail)).thenReturn(false);
        when(userRepository.existsByUserName(username)).thenReturn(false);
        when(userMapper.toUser(inputDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(outputDTO);

        UserOutputDTO result = userService.addUser(inputDTO);

        assertEquals(1L, result.getUserId());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        Long userId = 1L;
        String newName = "New Name";

        User existingUser = new User();
        existingUser.setUserId(userId);

        UserUpdateDTO input = new UserUpdateDTO(newName, null, null,
                null, null, null);

        existingUser.setUserFullName(newName);

        UserOutputDTO output = new UserOutputDTO(userId, newName, null, null,
                null, null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toDto(existingUser)).thenReturn(output);

        UserOutputDTO result = userService.updateUser(userId, input);

        assertEquals(newName, result.getUserFullName());
    }

    @Test
    @DisplayName("Should delete user by id")
    void shouldDeleteUserById() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);

        when(userRepository.existsById(userId)).thenReturn(true);
        userService.deleteUserById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should change user role")
    void shouldChangeUserRole() {
        Long userId = 1L;
        UserRole newRole = UserRole.PREMIUM;

        User targetUser = new User();
        targetUser.setUserId(userId);
        targetUser.setUserRole(BASIC);

        User requestingUser = new User();
        requestingUser.setUserId(2L);
        requestingUser.setUserRole(ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(targetUser));

        userService.changeUserRole(userId, newRole.name(), requestingUser);

        assertEquals(newRole, targetUser.getUserRole());
        verify(userRepository).save(targetUser);
    }

    @Test
    @DisplayName("Should return users by role")
    void shouldReturnUsersByRole () {
        String role = "ADMIN";

        User user = new User();
        UserOutputDTO dto = new UserOutputDTO();

        List<User> users = List.of(user);
        List<UserOutputDTO> userOutputDTOS = List.of(dto);

        when(userRepository.findAllByUserRole(UserRole.ADMIN)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(dto);

        List<UserOutputDTO> result = userService.getAllUsersByRole(role);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should search users")
    void shouldSearchUsers() {
        String query = "cat";

        User user = new User();
        UserOutputDTO dto = new UserOutputDTO();

        List<User> users = List.of(user);
        List<UserOutputDTO> expectedDtos = List.of(dto);

        when(userRepository.findByUserNameOrCatName(query)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(dto);

        List<UserOutputDTO> result = userService.getAllUsersByUserNameOrCatName(query);

        assertEquals(expectedDtos.size(), result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    @DisplayName("Should return user when username exists")
    void shouldReturnUserWhenUsernameExist() {
        User user = new User();
        when(userRepository.findByUserName("username")).thenReturn(Optional.of(user));
        User result = userService.findUserByUserName("username");
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Should throw not found exception when username not found")
    void shouldThrowNotFoundExceptionWhenUsernameNotFound() {
        when(userRepository.findByUserName("username")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findUserByUserName("username"));
    }

    @Test
    @DisplayName("Should return user when email exists")
    void shouldReturnUserWhenEmailExist() {
        User user = new User();
        when(userRepository.findByUserEmail("email@test.com")).thenReturn(Optional.of(user));
        User result = userService.findUserByEmail("email@test.com");
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Should throw not found exception when email not found")
    void shouldThrNotFoundExceptionWhenEmailNotFound() {
        when(userRepository.findByUserEmail("email@test.com")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findUserByEmail("email@test.com"));
    }

    @Test
    @DisplayName("Should return user DTO when username exists")
    void shouldReturnUserDtoWhenUsernameExists() {
        User user = new User();
        UserOutputDTO dto = new UserOutputDTO();
        when(userRepository.findByUserName("username")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);
        UserOutputDTO result = userService.getUserDtoByUserName("username");
        assertEquals(dto, result);
    }

    @Test
    @DisplayName("Should return user DTO when email exists")
    void shouldReturnUserDtoWhenEmailExists() {
        User user = new User();
        UserOutputDTO dto = new UserOutputDTO();
        when(userRepository.findByUserEmail("email@test.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);
        UserOutputDTO result = userService.getUserDtoByEmail("email@test.com");
        assertEquals(dto, result);
    }

    @Test
    @DisplayName("Should add user when given full User object")
    void shouldAddUserWhenGivenFullUserObject() {
        User user = new User();
        user.setUserName("username");
        user.setUserEmail("email@test.com");

        when(userRepository.existsByUserEmail("email@test.com")).thenReturn(false);
        when(userRepository.existsByUserName("@username")).thenReturn(false); // OBS! @ här
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.addUser(user);

        assertEquals("@username", result.getUserName());
        verify(userRepository).save(any(User.class));
    }


    @Test
    @DisplayName("Should partially update user fields")
    void shouldPartiallyUpdateUserFields() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        Map<String, Object> updates = Map.of("userFullName", "Updated Name");
        User result = userService.updateUser(1L, updates);
        assertEquals("Updated Name", result.getUserFullName());
    }

    @Test
    @DisplayName("Should throw when username already exists in partial update")
    void shouldThrowNullPointerExceptionWhenUsernameAlreadyExistsInPartialUpdate() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUserName("taken")).thenReturn(Optional.of(new User()));

        Map<String, Object> updates = Map.of("userName", "taken");
        assertThrows(NullPointerException.class, () -> userService.updateUser(1L, updates));
    }

    @Test
    @DisplayName("Should throw when email already exists in partial update")
    void shouldThrowNullPointerExceptionWhenEmailAlreadyExistsInPartialUpdate() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUserEmail("taken@test.com")).thenReturn(Optional.of(new User()));

        Map<String, Object> updates = Map.of("userEmail", "taken@test.com");
        assertThrows(NullPointerException.class, () -> userService.updateUser(1L, updates));
    }

    @Test
    @DisplayName("Should return all users as DTO list")
    void shouldReturnAllUsersAsDtoList() {
        User user = new User();
        UserOutputDTO dto = new UserOutputDTO();
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);
        List<UserOutputDTO> result = userService.getAllUsers();
        assertEquals(1, result.size());
    }
}
