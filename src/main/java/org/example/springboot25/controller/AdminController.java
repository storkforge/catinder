package org.example.springboot25.controller;

import org.example.springboot25.entities.User;
import org.example.springboot25.service.PostService;
import org.example.springboot25.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final PostService postService;
    private final UserService userService;

    public AdminController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // Admin Dashboard)
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    // User List
    @GetMapping("/users")
    public String adminUserList(Model model) {
        try {
            model.addAttribute("users", userService.getAllUsers());
            return "admin/users";
        } catch (Exception e) {
            log.warn("Failed to retrieve users: {}", e.getMessage());
            model.addAttribute("error", "Failed to retrieve users");
            return "admin/error";
        }
    }

    // Edit user form
    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    // Update user
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user.getUserId(), user);
        return "redirect:/admin/users";
    }

    // View Logs
    @GetMapping("/logs")
    public String viewLogs(Model model) {
        model.addAttribute("logs", List.of(
                "User eva logged in",
                "User admin created a post",
                "User catlover updated profile"
        ));
        return "admin/logs";
    }

    @PostMapping("/users/role")
    public String changeUserRole(@RequestParam Long userId, @RequestParam String newRole) {
        try {
            userService.changeUserRole(userId, newRole);
            return "redirect:/admin/settings?success=role-changed";
        } catch (Exception e) {
            // Log the error
            return "redirect:/admin/settings?error=role-change-failed";
        }
    }

    // Visa alla inlägg (för moderering)
    @GetMapping("/posts")
    public String showAllPosts(Model model) {
        try {
            model.addAttribute("posts", postService.getAllPosts());
            return "admin/posts"; // t.ex. templates/admin/posts.html
        } catch (Exception e) {
            // Log the error (optional)
            model.addAttribute("error", "Failed to retrieve posts");
            return "admin/error"; // Create this view if not exists
        }
    }

    // Ta bort ett inlägg
    @PostMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return "redirect:/admin/posts";
        } catch (Exception e) {
            log.warn("Failed to delete post with id {}: {}", postId, e.getMessage());
            return "redirect:/admin/posts?error=delete-failed";
        }
    }

    // Ta bort en användare
    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return "redirect:/admin/users";
        } catch (Exception e) {
            // Log the error (you can also log e.getMessage() for specifics)
            log.warn("Failed to delete user with id {}: {}", userId, e.getMessage());
            return "redirect:/admin/users?error=delete-failed";
        }
    }

    // Systeminställningar – placeholder för framtida grejer
    @GetMapping("/settings")
    public String settingsPage() {
        return "admin/settings"; // t.ex. templates/admin/settings.html
    }
}
