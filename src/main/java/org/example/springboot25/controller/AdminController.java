package org.example.springboot25.controller;

import org.example.springboot25.service.PostService;
import org.example.springboot25.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @PreAuthorize("hasRole('ADMIN')")
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
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/posts/delete/{postId}")
        public String deletePost (@PathVariable Long postId){
            postService.deletePost(postId);
            return "redirect:/admin/posts";
        }

        // Visa alla användare
        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/users")
        public String showAllUsers (Model model){
            model.addAttribute("users", userService.getAllUsers());
            return "admin/users"; // t.ex. templates/admin/users.html
        }

        // Ta bort en användare
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/users/delete/{userId}")
        public String deleteUser (@PathVariable Long userId){
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
        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/settings")
        public String settingsPage () {
            return "admin/settings"; // t.ex. templates/admin/settings.html
        }
    }
