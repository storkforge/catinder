package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.service.UserService;
import org.example.springboot25.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserViewController {

    private static final Logger log = LoggerFactory.getLogger(UserViewController.class);
    private final UserService userService;
    private final UserMapper userMapper;

    public UserViewController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
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
}
