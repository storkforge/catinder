package org.example.springboot25.controller;

import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.PostService;
import org.example.springboot25.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;
    private final UserService userService;

    // Visa alla inlägg (för moderering)
    @GetMapping("/posts")
    public String showAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "admin/posts"; // t.ex. templates/admin/posts.html
    }

    // Ta bort ett inlägg
    @PostMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/admin/posts";
    }

    // Visa alla användare
    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users"; // t.ex. templates/admin/users.html
    }

    // Ta bort en användare
    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin/users";
    }

    // Systeminställningar – placeholder för framtida grejer
    @GetMapping("/settings")
    public String settingsPage() {
        return "admin/settings"; // t.ex. templates/admin/settings.html
    }
}
