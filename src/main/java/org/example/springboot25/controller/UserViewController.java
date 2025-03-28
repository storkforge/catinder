package org.example.springboot25.controller;

import org.example.springboot25.entities.User;
import org.example.springboot25.service.UserService;
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

    @GetMapping("/users")
    public String users(Model model) {
        try {
            List<User> allUsers = userService.getAllUsers();
            model.addAttribute("users", allUsers);
        } catch (Exception e) {
            model.addAttribute("users", "Unable to retrieve user list");
        }
        return "user/user-list";
    }

    //Todo: Fix custom exceptions and error handling in UserService
    @GetMapping
    public String userById(@RequestParam Long userId, Model model) {
        try {
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            return "user/user-details";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/by-name")
    String usersByFullName(@RequestParam String fullName, Model model) {
        try {
            List<User> users = userService.getAllByFullName("%" + fullName + "%");
            model.addAttribute("users", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping
    String userByUserName(@RequestParam String userName, Model model) {
        try {
            User user = userService.getByUserName(userName);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-details";
    }

    //Todo: dont know if email string will work normally
    @GetMapping
    String userByUserEmail(@RequestParam String userEmail, Model model) {
        try {
            User user = userService.getByEmail(userEmail);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-details";
    }

    @GetMapping("/by-username")
    String usersByUserName(@RequestParam String userName, Model model) {
        try {
            List<User> users = userService.getAllByUserName("%" + userName + "%");
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-location")
    String usersByUserLocation(@RequestParam String userLocation, Model model) {
        try {
            List<User> users = userService.getAllByLocation(userLocation);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-role")
    String usersByUserRole(@RequestParam String userRole, Model model) {
        try {
            List<User> users = userService.getAllByRole(userRole);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-role-and-location")
    String usersByRoleAndLocation(@RequestParam String userRole, @RequestParam String userLocation, Model model) {
        try {
            List<User> users = userService.getAllByRoleAndLocation(userRole, userLocation);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-cat")
    String usersByCatName(@RequestParam String catName, Model model) {
        try {
            List<User> users = userService.getAllByCatName(catName);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-search-term")
    String usersBySearchTerm(@RequestParam String searchTerm, Model model) {
        try {
            List<User> users = userService.getAllByUserNameOrCatName(searchTerm);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/users/add")
    String addUser(Model model) {
        if (model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "user/user-add";
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
        return "user/user-update";
    }

    //Todo: PatchMapping is not supported? Look up update
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
