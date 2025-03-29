package org.example.springboot25.service;

import jakarta.transaction.Transactional;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {
    Logger log = LoggerFactory.getLogger(UserService.class);

    UserRepository userRepository;

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
                .orElseThrow(() -> new NotFoundException("User with name" + userName + " not found"));
    }

    public User getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User with email " + userEmail + " not found"));
    }

    public List<User> getAllUsersByUserName(String userName) {
        List<User> users = userRepository.findAllByUserName(userName);
        if (users.isEmpty()) {
            throw new NotFoundException("No user with username " + userName + " was found");
        }
        return users;
    }

    public List<User> getAllUsersByFullName(String fullName) {
        List<User> users = userRepository.findByFullName(fullName);
        if (users.isEmpty()) {
            throw new NotFoundException("No user with name " + fullName + " was found");
        }
        return users;
    }

    public List<User> getAllUsersByLocation(String userLocation) {
        List<User> users = userRepository.findAllByLocation(userLocation);
        if (users.isEmpty()) {
            throw new NotFoundException("No user with location " + userLocation + " was found");
        }
        return users;
    }

    public List<User> getAllUsersByRole(String userRole) {
        List<User> users = userRepository.findAllByRole(userRole);
        if (users.isEmpty()) {
            throw new NotFoundException("No user with name " + userRole + " was found");
        }
        return users;
    }

    public List<User> getAllUsersByRoleAndLocation(String userRole, String userLocation) {
        List<User> users = userRepository.findAllByRoleAndLocation(userRole, userLocation);
        if (users.isEmpty()) {
            throw new NotFoundException("No user with role " + userRole + " and location " + userLocation + " was found");
        }
        return users;
    }

    public List<User> getAllUsersByCatName(String catName) {
        List<User> users = userRepository.findAllUsersByCatName(catName);
        if (users.isEmpty()) {
            throw new NotFoundException("No user with belonging cat " + catName + " was found");
        }
        return users;
    }

    public List<User> getAllUsersByUserNameOrCatName(String searchTerm) {
        List<User> users = userRepository.findUsersByUsernameOrCatName(searchTerm);
        if (users.isEmpty()) {
            throw new NotFoundException("No user with username/cat " + searchTerm + " was found");
        }
        return users;
    }

    public User addUser(User user) {
        log.info("Creating new user: {}", user.getUserName());
        return userRepository.save(user);
    }

    /**
     * Updates an existing user by replacing its properties with those from the given user object.
     * <p>
     * This method retrieves the current user with the specified ID, updates all relevant fields
     * (userName, fullName, email, etc.) with the values from the provided user object, and then saves
     * the updated entity. It is intended for full updates where all fields are provided.
     * </p>
     *
     * @param user the User object containing the updated details
     * @param userId   the ID of the user to update
     * @return the updated User
     * @throws IllegalArgumentException if no user exists with the provided ID
     */
    public User updateUser( Long userId, User user) {
        var oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
        log.info("Updating user: {}", user.getUserName());
        oldUser.setUserName(user.getUserName());
        oldUser.setFullName(user.getFullName());
        oldUser.setUserEmail(user.getUserEmail());
        oldUser.setUserLocation(user.getUserLocation());
        oldUser.setUserRole(user.getUserRole());
        oldUser.setUserAuthProvider(user.getUserAuthProvider());
        return userRepository.save(oldUser);
    }

    /**
     * Partially updates an existing user based on the provided field changes.
     * <p>
     * This method retrieves the current user with the specified ID and then updates only those fields
     * present in the given map. This allows for patching individual fields without needing to send a full
     * User object.
     * </p>
     *
     * @param userId      the ID of the user to update
     * @param updates a map where keys are field names (e.g., "userName", "fullName") and values are the new values for those fields
     * @return the updated User
     * @throws IllegalArgumentException if no user exists with the provided ID
     */
    public User updateUser(Long userId, Map<String, Object> updates) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
        log.info("Updating user: {}", existingUser.getUserName());
        if (updates.containsKey("fullName")) {
            existingUser.setFullName((String) updates.get("fullName"));
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
        log.info("Deleting user with id: {}", userId);
        userRepository.deleteById(userId);
    }
}
