package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
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

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    private boolean isNotOwnerOrAdmin(Long targetUserId, User currentUser) {
        boolean isOwner = targetUserId.equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserOutputDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/id/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDTO getById(@PathVariable Long userId, Authentication auth) {
        User current = userService.getUserByUserName(auth.getName());
        UserOutputDTO target = userService.getUserById(userId);

        if (isNotOwnerOrAdmin(target.getUserId(), current)) {
            throw new AccessDeniedException("You are not owner or admin");
        }

        return target;
    }

    @GetMapping("/username/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDTO getByUserName(@PathVariable String userName) {
        return userService.getUserDtoByUserName(userName);
    }

    @GetMapping("/by-username/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserOutputDTO> getAllByUserName(@PathVariable String userName) {
        return userService.getAllUsersByUserName(userName);
    }

    @GetMapping("/by-name/{userFullName}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserOutputDTO> getAllByFullName(@PathVariable String userFullName) {
        return userService.getAllUsersByFullName(userFullName);
    }

    @GetMapping("/by-location/{userLocation}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserOutputDTO> getAllByLocation(@PathVariable String userLocation) {
        return userService.getAllUsersByLocation(userLocation);
    }

    @GetMapping("/by-role/{userRole}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserOutputDTO> getAllByRole(@PathVariable String userRole) {
        return userService.getAllUsersByRole(userRole);
    }

    @GetMapping("/by-search-term/{searchTerm}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserOutputDTO> getAllByUserNameOrCatName(@PathVariable String searchTerm) {
        return userService.getAllUsersByUserNameOrCatName(searchTerm);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutputDTO addUser(@RequestBody @Valid UserInputDTO userInputDTO) {
        return userService.addUser(userInputDTO);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserOutputDTO updateUser(@PathVariable Long userId,
                                    @RequestBody @Valid UserUpdateDTO userUpdateDTO,
                                    Authentication auth) {
        User current = userService.getUserByUserName(auth.getName());
        UserOutputDTO target = userService.getUserById(userId);

        if (isNotOwnerOrAdmin(target.getUserId(), current)) {
            throw new AccessDeniedException("You are not owner or admin");
        }

        return userService.updateUser(userId, userUpdateDTO);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId, Authentication auth) {
        User current = userService.getUserByUserName(auth.getName());
        UserOutputDTO target = userService.getUserById(userId);

        if (isNotOwnerOrAdmin(target.getUserId(), current)) {
            throw new AccessDeniedException("You can only delete your own account.");
        }

        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
