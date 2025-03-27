package org.example.springboot25.controller;

import org.example.springboot25.entities.User;
import org.example.springboot25.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        try {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        } catch (Exception e) {
            model.addAttribute("users", "Unable to retrieve user list");
        }
        return "user-list";
    }

    @GetMapping("/{userId}")
    public String user(@PathVariable Long userId, Model model) {
        try {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "user-details";
            } else {
                model.addAttribute("error", "User not found");
                return "error-page";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }


    @GetMapping("/byName/")
    @ResponseBody
    Optional<User> user(Model model) {
//        try {
//
//        }
//        return userService.getAllUsers();
        return null;
    }


}
