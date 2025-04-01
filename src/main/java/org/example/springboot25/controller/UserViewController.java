package org.example.springboot25.controller;

import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserViewController {
    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            model.addAttribute("usersError", "Unable to retrieve user list");
        }
        model.addAttribute("users", allUsers);
        return "user/user-list";
    }

    @GetMapping("/id/{userId}")
    public String getUserById(@PathVariable() Long userId, Model model) {
        try {
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            return "user/user-details";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/username/{userName}")
    String getUserByUserName(@PathVariable String userName, Model model) {
        try {
            User user = userService.getUserByUserName(userName);
            model.addAttribute("user", user);
            return "user/user-details";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/by-email/{userEmail}")
    String getUserByUserEmail(@PathVariable String userEmail, Model model) {
        try {
            User user = userService.getUserByEmail(userEmail);
            model.addAttribute("user", user);
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
        return "user/user-details";
    }

    @GetMapping("/by-username/{userName}")
    String getUsersByUserName(@PathVariable String userName, Model model) {
        List<User> users = userService.getAllUsersByUserName("%" + userName + "%");
        if (users.isEmpty())
            model.addAttribute("message", "No users found with username '" + userName + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-name/{userFullName}")
    String getUsersByFullName(@PathVariable String userFullName, Model model) {
        List<User> users = userService.getAllUsersByFullName("%" + userFullName + "%");
        if (users.isEmpty())
            model.addAttribute("message", "No users found for name '" + userFullName + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-location/{userLocation}")
    String getUsersByUserLocation(@PathVariable String userLocation, Model model) {
        List<User> users = userService.getAllUsersByLocation(userLocation);
        if (users.isEmpty())
            model.addAttribute("message", "No users found for location '" + userLocation + "'.");
        model.addAttribute("users", users);

        return "user/user-list";
    }

    @GetMapping("/by-role")
    public String getUsersByUserRole(@RequestParam String userRole, Model model) {
        List<User> users = userService.getAllUsersByRole(userRole);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for role '" + userRole + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }


    @GetMapping("/by-role-location")
    String getUsersByRoleAndLocation(@RequestParam String userRole, @RequestParam String userLocation, Model model) {
        List<User> users = userService.getAllUsersByRoleAndLocation(userRole, userLocation);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for role '" + userRole + "' and location '" + userLocation + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-cat")
    String getUsersByCatName(@RequestParam String catName, Model model) {
        List<User> users = userService.getAllUsersByCatName(catName);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for cat '" + catName + "'.");

        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-search-term/{searchTerm}")
    String getUsersByUserNameOrCatName(@PathVariable String searchTerm, Model model) {
        List<User> users = userService.getAllUsersByUserNameOrCatName(searchTerm);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for search term '" + searchTerm + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/add")
    String addUser(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "user/user-add";
    }

    @PostMapping("/add")
    String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userService.addUser(user);
        redirectAttributes.addFlashAttribute("Success", true);
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/users/add";
    }

    @GetMapping("/update")
    String updateUser(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "user/user-update";
    }

    @PatchMapping("/update")
    String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userService.updateUser(user.getUserId(), user);
        redirectAttributes.addFlashAttribute("Success", true);
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/users/update";
    }

    @GetMapping("/delete/{userId}")
    String deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/";
    }

}
