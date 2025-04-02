package org.example.springboot25.service;

import jakarta.transaction.Transactional;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.exceptions.UserAlreadyExistsException;
import org.example.springboot25.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User with username " + userName + " not found"));
    }

    public User getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User with email " + userEmail + " not found"));
    }

    public List<User> getAllUsersByUserName(String userName) {
        return userRepository.findAllByUserName(userName);
    }

    public List<User> getAllUsersByFullName(String fullName) {
        return userRepository.findAllByUserFullName(fullName);
    }

    public List<User> getAllUsersByLocation(String userLocation) {
        return userRepository.findAllByLocation(userLocation);
    }

    public List<User> getAllUsersByRole(String userRole) {
        return userRepository.findAllByRole(userRole);
    }

    public List<User> getAllUsersByRoleAndLocation(String userRole, String userLocation) {
        return userRepository.findAllByRoleAndLocation(userRole, userLocation);
    }

    public List<User> getAllUsersByCatName(String catName) {
        return userRepository.findAllUsersByCatName(catName);
    }

    public List<User> getAllUsersByUserNameOrCatName(String searchTerm) {
        return userRepository.findUsersByUsernameOrCatName(searchTerm);
    }

    public User addUser(User user) {
        if (userRepository.existsByUserEmail(user.getUserEmail()))
            throw new UserAlreadyExistsException("Account with given email already exists.");
        if (userRepository.existsByUserName(user.getUserName()))
            throw new UserAlreadyExistsException("Username is taken.");
        log.info("Creating new user: {}", user.getUserName());
        return userRepository.save(user);
    }

    /**
     * Full update of a user.
     * Retrieves the existing user by id, updates all fields, and saves.
     */
    public User updateUser(Long userId, User user) {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found."));
        // Check if the email belongs to a different user.
        Optional<User> userByEmail = userRepository.findByUserEmail(user.getUserEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getUserId().equals(userId)) {
            throw new UserAlreadyExistsException("Account with given email already exists.");
        }

        // Check if the username belongs to a different user.
        Optional<User> userByName = userRepository.findByUserName(user.getUserName());
        if (userByName.isPresent() && !userByName.get().getUserId().equals(userId)) {
            throw new UserAlreadyExistsException("Username is taken.");
        }
        log.info("Updating user: {}", user.getUserName());
        oldUser.setUserName(user.getUserName());
        oldUser.setUserFullName(user.getUserFullName());
        oldUser.setUserEmail(user.getUserEmail());
        oldUser.setUserLocation(user.getUserLocation());
        oldUser.setUserRole(user.getUserRole());
        oldUser.setUserAuthProvider(user.getUserAuthProvider());
        return userRepository.save(oldUser);
    }

    /**
     * Partially updates a user based on provided field changes.
     */
    public User updateUser(Long userId, Map<String, Object> updates) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found."));

        if (updates.containsKey("userEmail")) {
            String newEmail = updates.get("userEmail").toString();
            Optional<User> userByEmail = userRepository.findByUserEmail(newEmail);
            if (userByEmail.isPresent() && !userByEmail.get().getUserId().equals(userId)) {
                throw new UserAlreadyExistsException("Account with given email already exists.");
            }
        }

        if (updates.containsKey("userName")) {
            String newUserName = updates.get("userName").toString();
            Optional<User> userByName = userRepository.findByUserName(newUserName);
            if (userByName.isPresent() && !userByName.get().getUserId().equals(userId)) {
                throw new UserAlreadyExistsException("Username is taken.");
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
            existingUser.setUserRole((String) updates.get("userRole"));
        }
        if (updates.containsKey("userAuthProvider")) {
            existingUser.setUserAuthProvider((String) updates.get("userAuthProvider"));
        }
        return userRepository.save(existingUser);
    }


    public void deleteUserById(Long userId) {
        log.info("Deleting user with id: {}", userId + ".");
        userRepository.deleteById(userId);
    }
}
