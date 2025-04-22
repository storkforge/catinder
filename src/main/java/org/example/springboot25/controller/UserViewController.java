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
import org.example.springboot25.security.CustomUserDetails;
import org.example.springboot25.security.CustomUserDetailsService;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.example.springboot25.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private final CustomUserDetailsService uds;
    private final CatService catService;
    private final UserMapper userMapper;

    public UserViewController(UserService userService,
                              CatService catService,
                              CustomUserDetailsService uds,
                              UserMapper userMapper) {
        this.userService = userService;
        this.catService = catService;
        this.uds = uds;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String getUsers(Model model) {
        List<UserOutputDTO> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            model.addAttribute("usersError", "No users found.");
        }
        model.addAttribute("users", allUsers);
        return "user/user-list";
    }

    @GetMapping("/profile/id/{userId}")
    public String getUserById(@PathVariable Long userId, Model model) {
        try {
            UserOutputDTO user = userService.getUserDtoById(userId);
            User userEntity = userMapper.toUser(user);
            List<Cat> cats = catService.getAllCatsByUser(userEntity);
            model.addAttribute("user", user);
            model.addAttribute("cats", cats);
            return "user/user-details";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/profile/{userName}")
    public String getUserByUserName(@PathVariable String userName, Model model) {
        try {
            User user = userService.findUserByUserName(userName);
            model.addAttribute("user", user);
            return "user/user-details";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/by-email/{userEmail}")
    public String getUserByUserEmail(@PathVariable String userEmail, Model model) {
        try {
            User user = userService.findUserByEmail(userEmail);
            model.addAttribute("user", user);
            return "user/user-details";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/by-username/{userName}")
    public String getUsersByUserName(@PathVariable String userName, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByUserName("%" + userName + "%");
        if (users.isEmpty())
            model.addAttribute("message", "No users found with username '" + userName + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-name/{userFullName}")
    public String getUsersByFullName(@PathVariable String userFullName, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByFullName("%" + userFullName + "%");
        if (users.isEmpty())
            model.addAttribute("message", "No users found for name '" + userFullName + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-location/{userLocation}")
    public String getUsersByUserLocation(@PathVariable String userLocation, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByLocation(userLocation);
        if (users.isEmpty())
            model.addAttribute("message", "No users found for location '" + userLocation + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-role/{userRole}")
    public String getUsersByUserRole(@PathVariable String userRole, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByRole(userRole);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for role '" + userRole + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-role-location")
    public String getUsersByRoleAndLocation(@RequestParam String userRole, @RequestParam String userLocation, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByRoleAndLocation(userRole, userLocation);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for role '" + userRole + "' and location '" + userLocation + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-cat")
    public String getUsersByCatName(@RequestParam String catName, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByCatName(catName);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for cat '" + catName + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-search-term/{searchTerm}")
    public String getUsersByUserNameOrCatName(@PathVariable String searchTerm, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByUserNameOrCatName(searchTerm);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for search term '" + searchTerm + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new UserInputDTO());
        return "user/user-add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") @Valid UserInputDTO inputDTO, Model model) {
        try {
            userService.addUser(inputDTO);
            return "redirect:/users/list?success=added";
        } catch (Exception e) {
            log.error("Failed to add user", e);
            model.addAttribute("error", "Failed to add user");
            return "user/user-add";
        }
    }

    @GetMapping("/{userId}/edit")
    public String editUserForm(@PathVariable Long userId, Model model) {
        User current = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.checkIfOwnerOrAdmin(userId, current);

        try {
            UserOutputDTO userDTO = userService.getUserDtoById(userId);
            UserUpdateDTO updateDTO = userMapper.outputToUpdateDTO(userDTO);
            model.addAttribute("user", updateDTO);
            model.addAttribute("userId", userId);
            return "user/user-update";
        } catch (NotFoundException ex) {
            log.error("Could not load user {}", userId, ex);
            model.addAttribute("error", "User not found");
            return "error";
        }
    }

    @PutMapping("/{userId}/edit")
    public String updateUser(@PathVariable Long userId,
                             @Valid @ModelAttribute("user") UserUpdateDTO updateDTO,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        User current = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.checkIfOwnerOrAdmin(userId, current);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userId);
            return "user/user-update";
        }
        try {
            userService.updateUser(userId, updateDTO);
            redirectAttributes.addFlashAttribute("update_success", "Saved!");

            Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails freshDetails = new CustomUserDetails(userMapper.toUser(updateDTO));

            Authentication newAuth;
            if (oldAuth instanceof OAuth2AuthenticationToken oauth) {
                newAuth = new OAuth2AuthenticationToken(
                        oauth.getPrincipal(),
                        freshDetails.getAuthorities(),
                        oauth.getAuthorizedClientRegistrationId());
            } else {
                newAuth = new UsernamePasswordAuthenticationToken(
                        freshDetails,
                        oldAuth.getCredentials(),
                        freshDetails.getAuthorities());
            }
            ((AbstractAuthenticationToken) newAuth)
                    .setDetails(((AbstractAuthenticationToken) oldAuth).getDetails());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } catch (Exception ex) {
            log.error("Failed to update user {}", userId, ex);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/users/" + userId + "/edit";
        }

        return "redirect:/users/" + userId + "/edit";
    }

    @PatchMapping("/{userId}/edit")
    public String updateUser(@PathVariable Long userId,
                             @RequestParam Map<String, Object> updates,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        User current = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.checkIfOwnerOrAdmin(userId, current);

        try {
            userService.updateUser(userId, updates);
            redirectAttributes.addFlashAttribute("update_success", "Details saved!");
        } catch (AlreadyExistsException | NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
        return "redirect:/users/" + userId + "/edit";
    }

    @DeleteMapping("/{userId}/delete")
    public String deleteOwnAccount(@PathVariable Long userId,
                                   HttpServletRequest request,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        User current = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.checkIfOwnerOrAdmin(userId, current);

        try {
            userService.deleteUserById(userId);
            redirectAttributes.addFlashAttribute("delete_success", "Account deleted!");
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
        return "redirect:/";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserFromList(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return "redirect:/users/list?success=deleted";
        } catch (Exception e) {
            log.warn("Failed to delete user {}", id, e);
            return "redirect:/users/list?error=delete-failed";
        }
    }
}
