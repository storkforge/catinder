package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // Helper method for access control
    private boolean isNotOwnerOrAdmin(User targetUser, User currentUser) {
        boolean isOwner = targetUser.getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    // Only ADMIN can get list of all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Authenticated user can get their own profile or ADMIN can get any
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/id/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getById(@PathVariable Long userId, Authentication auth) {
        User target = userService.getUserById(userId);
        User current = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(target, current)) {
            throw new AccessDeniedException("You are not owner or admin");
        }

        return target;
    }

    // Public (no auth): useful for username check, or you can lock it down
    @GetMapping("/username/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public User getByUserName(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }

    // These GETs can remain public or restricted, depending on project need
    @GetMapping("/by-username/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByUserName(@PathVariable String userName){
        return userService.getAllUsersByUserName(userName);
    }

    @GetMapping("/by-name/{userFullName}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByFullName(@PathVariable String userFullName) {
        return userService.getAllUsersByFullName(userFullName);
    }

    @GetMapping("/by-location/{userLocation}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByLocation(@PathVariable String userLocation) {
        return userService.getAllUsersByLocation(userLocation);
    }

    @GetMapping("/by-role/{userRole}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByRole(@PathVariable String userRole) {
        return userService.getAllUsersByRole(userRole);
    }

    @GetMapping("/by-search-term/{searchTerm}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByUserNameOrCatName(@PathVariable String searchTerm) {
        return userService.getAllUsersByUserNameOrCatName(searchTerm);
    }

    // Registration should be open
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    // Only the user themselves or an admin can update profile
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long userId, @RequestBody @Valid User user, Authentication auth) {
        User target = userService.getUserById(userId);
        User current = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(target, current)) {
            throw new AccessDeniedException("You are not owner or admin");
        }

        return userService.updateUser(userId, user);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long userId, @RequestBody Map<String, Object> updates, Authentication auth) {
        User target = userService.getUserById(userId);
        User current = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(target, current)) {
            throw new AccessDeniedException("You are not owner or admin");
        }

        return userService.updateUser(userId, updates);
    }

    // Only user themselves or ADMIN can delete account
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId, Authentication auth) {
        User target = userService.getUserById(userId);
        User current = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(target, current)) {
            throw new AccessDeniedException("You can only delete your own account.");
        }

        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
