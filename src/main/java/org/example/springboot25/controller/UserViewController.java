package org.example.springboot25.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.exceptions.AlreadyExistsException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserViewController {
    private final UserService userService;
    private CatService catService;

    public UserViewController(UserService userService, CatService catService) {
        this.userService = userService;
        this.catService = catService;
    }

    @GetMapping
    public String getUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            model.addAttribute("usersError", "No users found.");
        }
        model.addAttribute("users", allUsers);
        return "user/user-list";
    }

    @GetMapping("/profile/id/{userId}")
    public String getUserById(@PathVariable() Long userId, Model model) {
        try {
            User user = userService.getUserById(userId);
            List<Cat> cats = catService.getAllCatsByUser(user);
            model.addAttribute("user", user);
            model.addAttribute("cats", cats);

            return "user/user-details";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/profile/{userName}")
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
            return "user/user-details";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
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

    @GetMapping("/by-role/{userRole}")
    public String getUsersByUserRole(@PathVariable String userRole, Model model) {
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
    String addUserForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "user/user-add";
    }

    @PostMapping("/add")
    String addUserForm(@Valid @ModelAttribute User user, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/user-add";
        }
        try {
            userService.addUser(user);
            redirectAttributes.addFlashAttribute("success", "Account created!");
        } catch (AlreadyExistsException ex) {
            model.addAttribute("error", ex.getMessage());
            return "user/user-add";
        }
        return "redirect:/users/profile/id/" + user.getUserId();
    }

    @GetMapping("/{userId}/edit")
    public String showUpdateForm(@PathVariable Long userId, Model model) {
        try {
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            return "user/user-update";
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error-page";
        }
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable Long userId, @Valid @ModelAttribute User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/user-update";
        }
        try {
            userService.updateUser(userId, user);
            redirectAttributes.addFlashAttribute("update_success", "Details saved!");
        } catch (AlreadyExistsException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/users/" + userId + "/edit";
        }
        return "redirect:/users/" + userId + "/edit";
    }

    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable Long userId, @RequestParam Map<String, Object> updates, RedirectAttributes redirectAttributes, Model model) {
        try {
            User updatedUser = userService.updateUser(userId, updates);
            redirectAttributes.addFlashAttribute("update_success", "Details saved!");
        } catch (AlreadyExistsException | NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error-page";
        }
        return "redirect:/users/" + userId + "/edit";
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable Long userId, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        try {
            userService.deleteUserById(userId);
            redirectAttributes.addFlashAttribute("delete_success", "Account deleted!");
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error-page";
        }
            return "redirect:/";
    }
}
