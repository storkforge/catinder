package org.example.springboot25.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.exceptions.AlreadyExistsException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.example.springboot25.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserViewController.class);
    private final UserService userService;
    private final CatService catService;
    private final UserMapper userMapper;

    public UserViewController(UserService userService, CatService catService) {
    public UserViewController(UserService userService, UserMapper userMapper) {
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
        this.userMapper = userMapper;
    }

    @GetMapping("/list")
    public String userList(Model model) {
        List<UserOutputDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new UserInputDTO());
        return "user-add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") @Valid UserInputDTO inputDTO, Model model) {
        try {
            userService.addUser(inputDTO);
            return "redirect:/user/list?success=added";
        } catch (Exception e) {
            log.error("Failed to add user", e);
            model.addAttribute("error", "Failed to add user");
            return "user-add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        try {
            UserOutputDTO userDTO = userService.getUserById(id);
            UserUpdateDTO updateDTO = userMapper.outputToUpdateDTO(userDTO);
            model.addAttribute("user", updateDTO);
            model.addAttribute("userId", id);
            return "user-edit";
        } catch (Exception e) {
            log.error("Could not load user {}", id, e);
            model.addAttribute("error", "User not found");
            return "user-error";
        }
    }

    @PutMapping("/{userId}/edit")
    public String updateUser(@PathVariable Long userId, @Valid @ModelAttribute User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/user-update";
        }
    @PostMapping("/edit")
    public String updateUser(@RequestParam Long userId,
                             @ModelAttribute("user") @Valid UserUpdateDTO updateDTO,
                             Model model) {
        try {
            userService.updateUser(userId, updateDTO);
            return "redirect:/user/list?success=updated";
        } catch (Exception e) {
            log.error("Failed to update user {}", userId, e);
            model.addAttribute("error", "Update failed");
            model.addAttribute("userId", userId);
            return "user-edit";
        }
        return "redirect:/users/{userId}/edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return "redirect:/user/list?success=deleted";
        } catch (Exception e) {
            log.warn("Failed to delete user {}", id, e);
            return "redirect:/user/list?error=delete-failed";
        }
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
