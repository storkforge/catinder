package org.example.springboot25.service;

import jakarta.transaction.Transactional;
import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.exceptions.AlreadyExistsException;
import org.example.springboot25.mapper.UserMapper;
import org.example.springboot25.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================
    // INTERNAL METHODS (Entity)
    // ==========================

    public void checkIfOwnerOrAdmin(Long targetUserId, User currentUser) {
        if (targetUserId == null || currentUser == null || currentUser.getUserId() == null) {
            throw new AccessDeniedException("Missing user data.");
        }
        boolean isOwner = targetUserId.equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Access denied: not owner or admin.");
        }
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User with username " + userName + " not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    // ==========================
    // EXTERNAL METHODS (DTO)
    // ==========================

    public UserOutputDTO getUserDtoById(Long userId) {
        return userMapper.toDto(findUserById(userId));
    }

    public UserOutputDTO getUserDtoByUserName(String userName) {
        return userMapper.toDto(findUserByUserName(userName));
    }

    public UserOutputDTO getUserDtoByEmail(String email) {
        return userMapper.toDto(findUserByEmail(email));
    }

    public List<UserOutputDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserOutputDTO addUser(UserInputDTO userInputDTO) {
        if (userRepository.existsByUserEmail(userInputDTO.getUserEmail())) {
            throw new AlreadyExistsException("Email is already in use.");
        }
        if (userRepository.existsByUserName(userInputDTO.getUserName())) {
            throw new AlreadyExistsException("Username is already taken.");
        }
        try {
            log.info("Creating user: {}", userInputDTO.getUserName());
            User user = userMapper.toUser(userInputDTO);
            User saved = userRepository.save(user);
            return userMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException("Username or email already exists.");
        }
    }

    public User addUser(User user) {
        if (userRepository.existsByUserEmail(user.getUserEmail()))
            throw new AlreadyExistsException("Account with given email already exists.");
        if (userRepository.existsByUserName(user.getUserName()))
            throw new AlreadyExistsException("Username is taken.");

        log.info("Creating new user: {}", user.getUserName());
        if (user.getUserPassword() != null && !user.getUserPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(user.getUserPassword());
            user.setUserPassword(hashedPassword);
        } else {
            user.setUserPassword(null);
        }
        return userRepository.save(user);
    }

    public UserOutputDTO updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = findUserById(userId);

        if (userUpdateDTO.getUserName() != null) {
            Optional<User> existingUser = userRepository.findByUserName(userUpdateDTO.getUserName());
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(userId)) {
                throw new AlreadyExistsException("Username is taken.");
            }
        }

        log.info("Updating user: {}", user.getUserName());
        userMapper.updateUserFromDto(userUpdateDTO, user);
        return userMapper.toDto(userRepository.save(user));
    }

    public User updateUser(Long userId, Map<String, Object> updates) {
        User existingUser = findUserById(userId);

        if (updates.containsKey("userEmail")) {
            String newEmail = updates.get("userEmail").toString();
            Optional<User> userByEmail = userRepository.findByUserEmail(newEmail);
            if (userByEmail.isPresent() && !userByEmail.get().getUserId().equals(userId)) {
                throw new AlreadyExistsException("Account with given email already exists.");
            }
        }

        if (updates.containsKey("userName")) {
            String newUserName = updates.get("userName").toString();
            Optional<User> userByName = userRepository.findByUserName(newUserName);
            if (userByName.isPresent() && !userByName.get().getUserId().equals(userId)) {
                throw new AlreadyExistsException("Username is taken.");
            }
        }

        log.info("Partially updating user: {}", existingUser.getUserName());
        if (updates.containsKey("userFullName")) {
            existingUser.setUserFullName((String) updates.get("userFullName"));
        }
        if (updates.containsKey("userName")) {
            existingUser.setUserName((String) updates.get("userName"));
        }
        if (updates.containsKey("userEmail")) {
            existingUser.setUserEmail((String) updates.get("userEmail"));
        }
        if (updates.containsKey("userLocation")) {
            existingUser.setUserLocation((String) updates.get("userLocation"));
        }
        if (updates.containsKey("userRole")) {
            Object roleObj = updates.get("userRole");
            if (roleObj instanceof String roleStr) {
                existingUser.setUserRole(UserRole.valueOf(roleStr));
            } else if (roleObj instanceof UserRole roleEnum) {
                existingUser.setUserRole(roleEnum);
            }
        }
        if (updates.containsKey("userAuthProvider")) {
            existingUser.setUserAuthProvider((String) updates.get("userAuthProvider"));
        }

        return userRepository.save(existingUser);
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        log.info("Deleting user with id: {}", userId);
        userRepository.deleteById(userId);
    }

    public void changeUserRole(Long targetUserId, String newRole, User requestingUser) {
        if (requestingUser.getUserRole() != UserRole.ADMIN) {
            throw new SecurityException("Only ADMIN users can change roles.");
        }

        User user = findUserById(targetUserId);
        user.setUserRole(UserRole.valueOf(newRole.toUpperCase()));
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

    public List<UserOutputDTO> getAllUsersByRoleAndLocation(String role, String location) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            return userRepository.findAllByRoleAndLocation(userRole.name(), location).stream()
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

    public List<UserOutputDTO> getAllUsersByCatName(String catName) {
        return userRepository.findAllUsersByCatName(catName).stream()
                .map(userMapper::toDto)
                .toList();
    }
}
