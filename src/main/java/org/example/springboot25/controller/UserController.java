package org.example.springboot25.controller;

import org.example.springboot25.entities.User;
import org.example.springboot25.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Map the root URL to a welcome page
//    @GetMapping("/home")
//    public String index(Model model) {
//        model.addAttribute("message", "Welcome to our Application!");
//        return "index"; // Thymeleaf template located at src/main/resources/templates/index.html
//    }

    // Map URL for displaying a list of users
    @GetMapping("/users")
    public String users(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "user-list"; // Resolves to src/main/resources/templates/user-list.html
    }

    @GetMapping("/user/{userId}")
    String hello(Model model){
        model.addAttribute("message", "Hello, World!");
        model.addAttribute("name","Martin");
        return "hello";
    }

    @GetMapping("/users")
    @ResponseBody
    List<User> user(Model model){
        return userService.getAllUsers();
    }


}
