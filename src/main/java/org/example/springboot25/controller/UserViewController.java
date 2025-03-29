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
    public String getUsers(Model model) {
        try {
            List<User> allUsers = userService.getAllUsers();
            model.addAttribute("users", allUsers);
        } catch (Exception e) {
            model.addAttribute("users", "Unable to retrieve user list");
        }
        return "user/user-list";
    }

    //Todo: Check with group how we wish to handle error-page
    @GetMapping
    public String getUserById(@RequestParam Long userId, Model model) {
        try {
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            return "user/user-details";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping
    String getUserByUserName(@RequestParam String userName, Model model) {
        try {
            User user = userService.getUserByUserName(userName);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-details";
    }

    //Todo: dont know if email string will work normally
    @GetMapping
    String getUserByUserEmail(@RequestParam String userEmail, Model model) {
        try {
            User user = userService.getUserByEmail(userEmail);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-details";
    }

    @GetMapping("/by-username")
    String getUsersByUserName(@RequestParam String userName, Model model) {
        try {
            List<User> users = userService.getAllUsersByUserName("%" + userName + "%");
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-name")
    String getUsersByFullName(@RequestParam String fullName, Model model) {
        try {
            List<User> users = userService.getAllUsersByFullName("%" + fullName + "%");
            model.addAttribute("users", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-location")
    String getUsersByUserLocation(@RequestParam String userLocation, Model model) {
        try {
            List<User> users = userService.getAllUsersByLocation(userLocation);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-role")
    String getUsersByUserRole(@RequestParam String userRole, Model model) {
        try {
            List<User> users = userService.getAllUsersByRole(userRole);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-role-location")
    String getUsersByRoleAndLocation(@RequestParam String userRole, @RequestParam String userLocation, Model model) {
        try {
            List<User> users = userService.getAllUsersByRoleAndLocation(userRole, userLocation);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-cat")
    String getUsersByCatName(@RequestParam String catName, Model model) {
        try {
            List<User> users = userService.getAllUsersByCatName(catName);
            model.addAttribute("user", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/user-list";
    }

    @GetMapping("/by-search-term")
    String getUsersByUserNameOrCatName(@RequestParam String searchTerm, Model model) {
        try {
            List<User> users = userService.getAllUsersByUserNameOrCatName(searchTerm);
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
        userService.updateUser(user.getUserId(), user);
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
