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
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/id/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getById(@PathVariable() Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/username/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public User getByUserName(@PathVariable() String userName) {
        return userService.getUserByUserName(userName);
    }

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long userId, @RequestBody @Valid User user) {
        return userService.updateUser(userId, user);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long userId, @RequestBody Map<String, Object> updates) {
        return userService.updateUser(userId, updates);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
