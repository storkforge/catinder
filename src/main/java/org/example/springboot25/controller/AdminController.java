package org.example.springboot25.controller;

import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.mapper.UserMapper;
import org.example.springboot25.service.PostService;
import org.example.springboot25.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final PostService postService;
    private final UserService userService;
    private final UserMapper userMapper;

    public AdminController(PostService postService, UserService userService, UserMapper userMapper) {
        this.postService = postService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Admin dashboard
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    // User list
    @GetMapping("/users")
    public String adminUserList(Model model) {
        try {
            List<UserOutputDTO> users = userService.getAllUsers();
            model.addAttribute("users", users);
            return "admin/users";
        } catch (Exception e) {
            log.warn("Failed to retrieve users: {}", e.getMessage());
            model.addAttribute("error", "Failed to retrieve users");
            return "admin/error";
        }
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        try {
            User user = userService.findUserById(id);
            UserUpdateDTO updateDTO = userMapper.toUserUpdateDTO(user);
            model.addAttribute("user", updateDTO);
            model.addAttribute("userId", id);
            return "admin/user-edit";
        } catch (Exception e) {
            log.error("Could not load user with ID {}: {}", id, e.getMessage());
            model.addAttribute("error", "User could not be found");
            return "admin/error";
        }
    }

    // Update user
    @PostMapping("/users/update")
    public String updateUser(@RequestParam("userId") Long userId,
                             @ModelAttribute("user") UserUpdateDTO userUpdateDTO,
                             Model model) {
        try {
            userService.updateUser(userId, userUpdateDTO);
            return "redirect:/admin/users?success=updated";
        } catch (IllegalArgumentException e) {
            log.warn("Invalid user ID: {}", userId, e);
            model.addAttribute("error", "User update failed: invalid ID.");
        } catch (Exception e) {
            log.error("Unexpected error during user update", e);
            model.addAttribute("error", "An unexpected error occurred.");
        }
        model.addAttribute("userId", userId);
        return "admin/user-edit";
    }

    // View system logs
    @GetMapping("/logs")
    public String viewLogs(Model model) {
        model.addAttribute("logs", List.of(
                "User eva logged in",
                "User admin created a post",
                "User catlover updated profile"
        ));
        return "admin/dashboard";
    }

    // Change user role
    @PostMapping("/users/role")
    public String changeUserRole(@RequestParam Long userId,
                                 @RequestParam String newRole,
                                 Authentication auth) {
        try {
            User requestingUser = userService.findUserByUserName(auth.getName());
            userService.changeUserRole(userId, newRole, requestingUser);
            return "redirect:/admin/settings?success=role-changed";
        } catch (Exception e) {
            log.error("Failed to change role for user {}: {}", userId, e.getMessage());
            return "redirect:/admin/settings?error=role-change-failed";
        }
    }

    // Show all posts (for moderation)
    @GetMapping("/posts")
    public String showAllPosts(Model model) {
        try {
            model.addAttribute("posts", postService.getAllPosts());
            return "admin/posts";
        } catch (Exception e) {
            log.error("Failed to retrieve posts", e);
            model.addAttribute("error", "Failed to retrieve posts");
            return "admin/error";
        }
    }

    // Delete a post
    @PostMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return "redirect:/admin/posts";
        } catch (Exception e) {
            log.warn("Failed to delete post {}: {}", postId, e.getMessage());
            return "redirect:/admin/posts?error=delete-failed";
        }
    }

    // Delete a user
    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return "redirect:/admin/users";
        } catch (Exception e) {
            log.warn("Failed to delete user {}: {}", userId, e.getMessage());
            return "redirect:/admin/users?error=delete-failed";
        }
    }

    // Admin settings page
    @GetMapping("/settings")
    public String settingsPage() {
        return "admin/settings";
    }
}