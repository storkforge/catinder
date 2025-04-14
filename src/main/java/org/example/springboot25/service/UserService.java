package org.example.springboot25.service;

import jakarta.transaction.Transactional;
import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.exceptions.AlreadyExistsException;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.UserMapper;
import org.example.springboot25.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserOutputDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserOutputDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        return userMapper.toDto(user);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User with username " + userName + " not found"));
    }

    public UserOutputDTO getUserDtoByUserName(String userName) {
        User user = getUserByUserName(userName);
        return userMapper.toDto(user);
    }

    public UserOutputDTO addUser(UserInputDTO userInputDTO) {
        if (userRepository.existsByUserEmail(userInputDTO.getUserEmail())) {
            throw new AlreadyExistsException("Email is already in use.");
        }
        if (userRepository.existsByUserName(userInputDTO.getUserName())) {
            throw new AlreadyExistsException("Username is already taken.");
        }

        log.info("Creating user: {}", userInputDTO.getUserName());
        User user = userMapper.toUser(userInputDTO);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserOutputDTO updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userMapper.updateUserFromDto(userUpdateDTO, user);
        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        log.info("Deleting user with id: {}", userId);
        userRepository.deleteById(userId);
    }

    public void changeUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        user.setUserRole(UserRole.valueOf(newRole));
        userRepository.save(user);
    }

    public List<UserOutputDTO> getAllUsersByUserName(String userName) {
        return userRepository.findAllByUserNameContainingIgnoreCase(userName).stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserOutputDTO> getAllUsersByFullName(String fullName) {
        return userRepository.findAllByUserFullNameContainingIgnoreCase(fullName).stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserOutputDTO> getAllUsersByLocation(String location) {
        return userRepository.findAllByUserLocationIgnoreCase(location).stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserOutputDTO> getAllUsersByRole(String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            return userRepository.findAllByUserRole(userRole).stream()
                    .map(userMapper::toDto)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Invalid user role: " + role);
        }
    }

    public List<UserOutputDTO> getAllUsersByUserNameOrCatName(String searchTerm) {
        return userRepository.findByUserNameOrCatName(searchTerm).stream()
                .map(userMapper::toDto)
                .toList();
    }
}