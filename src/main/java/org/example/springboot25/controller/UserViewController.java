package org.example.springboot25.controller;

import org.example.springboot25.entities.User;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserViewController {
    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    @ResponseStatus(HttpStatus.OK)
    public String users(Model model) {
        try {
            List<User> allUsers = userService.getAllUsers();
            model.addAttribute("users", allUsers);
        } catch (Exception e) {
            model.addAttribute("users", "Unable to retrieve user list");
        }
        return "user-list";
    }

    //Todo: Fix custom exceptions and error handling in UserService
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String userById(@PathVariable Long userId, Model model) {
        try {
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            return "user-details";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/byName/{fullName}")
    String usersByFullName(@PathVariable String fullName, Model model) {
        try {
            List<User> users = userService.getAllByFullName("%" + fullName + "%");
            model.addAttribute("users", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user-list";
    }

    @GetMapping("/{userName}")
    String userByUserName(@PathVariable String userName, Model model) {
        try {
            User user = userService.getByUserName(userName);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user-details";
    }

    //Todo: this is weird
    @GetMapping("/{userEmail}")
    String userByUserEmail(@PathVariable String userEmail, Model model) {
        try {
            User user = userService.getByEmail(userEmail);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user-details";
    }

    @GetMapping("/byUserName/{userName}")
    String usersByUserName(@PathVariable String userName, Model model) {
        try {
            List<User> users = userService.getAllByUserName("%" + userName + "%");
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user-list";
    }

    @GetMapping("/byLocation/{userLocation}")
    String usersByUserLocation(@PathVariable String userLocation, Model model) {
        try {
            List<User> users = userService.getAllByLocation(userLocation);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user-list";
    }

    @GetMapping("/byRole/{userRole}")
    String usersByUserRole(@PathVariable String userRole, Model model) {
        try {
            List<User> users = userService.getAllByRole(userRole);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user-list";
    }

    @GetMapping("/users/add")
    String addUser(Model model) {
        if (model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "user-add";
    }

    @PostMapping("/addUser")
    String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userService.addUser(user);
        redirectAttributes.addFlashAttribute("Success", true);
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/users/add";
    }

    @GetMapping("/users/update")
    String updateUser(Model model) {
        if (model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "user-update";
    }

    //Todo: PatchMapping is not supported?
    @PatchMapping("/users/update")
    String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userService.updateUser(user, user.getUserId());
        redirectAttributes.addFlashAttribute("Success", true);
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/users/update";
    }

    @GetMapping("/users/delete/{userId}")
    String deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/";
    }

}
