package org.example.springboot25.controller;

import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.PostService;
import org.example.springboot25.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;
    private final UserService userService;

    public AdminController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // Visa alla inlägg (för moderering)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/posts")
    public String showAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "admin/posts"; // t.ex. templates/admin/posts.html
    }

    // Ta bort ett inlägg
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/admin/posts";
    }

    // Visa alla användare
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users"; // t.ex. templates/admin/users.html
    }

    // Ta bort en användare
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin/users";
    }

    // Systeminställningar – placeholder för framtida grejer
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/settings")
    public String settingsPage() {
        return "admin/settings"; // t.ex. templates/admin/settings.html
    }
}
