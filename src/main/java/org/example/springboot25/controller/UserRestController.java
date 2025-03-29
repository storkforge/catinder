package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public User getById(@RequestParam Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public User getByUserName(@RequestParam String userName) {
        return userService.getUserByUserName(userName);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public User getByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/by-username")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByUserName(@RequestParam String userName){
        return userService.getAllUsersByUserName(userName);
    }

    @GetMapping("/by-name")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByFullName(@RequestParam String fullName) {
        return userService.getAllUsersByFullName(fullName);
    }

    @GetMapping("/by-location")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByLocation(@RequestParam String userLocation) {
        return userService.getAllUsersByLocation(userLocation);
    }

    @GetMapping("/by-role")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByRole(@RequestParam String userRole) {
        return userService.getAllUsersByRole(userRole);
    }

    @GetMapping("/by-role-location")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByRoleAndLocation(@RequestParam String userRole, @RequestParam String userLocation) {
        return userService.getAllUsersByRoleAndLocation(userRole, userLocation);
    }

    @GetMapping("/by-cat")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByCatName(@RequestParam String catName) {
        return userService.getAllUsersByCatName(catName);
    }

    @GetMapping("/by-search-term")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllByUserNameOrCatName(@RequestParam String searchTerm) {
        return userService.getAllUsersByUserNameOrCatName(searchTerm);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody Long userId, @RequestBody @Valid User user) {
        return userService.updateUser(userId, user);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody Long userId, @RequestBody Map<String, Object> updates) {
        return userService.updateUser(userId, updates);
    }

    //Delete user by id
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
