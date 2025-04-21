package org.example.springboot25.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.springboot25.dto.CatOutputDTO;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    public String getUserById(@PathVariable() Long userId, Model model) {
        try {
            UserOutputDTO user = userService.getUserDtoById(userId);
            User userEntity = userMapper.toUser(user);
            List<CatOutputDTO> cats = catService.getAllCatsByUser(userEntity);
            model.addAttribute("user", user);
            model.addAttribute("cats", cats);

            return "user/user-details";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/profile/{userName}")
    String getUserByUserName(@PathVariable String userName, Model model) {
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
    String getUserByUserEmail(@PathVariable String userEmail, Model model) {
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
    String getUsersByUserName(@PathVariable String userName, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByUserName("%" + userName + "%");
        if (users.isEmpty())
            model.addAttribute("message", "No users found with username '" + userName + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-name/{userFullName}")
    String getUsersByFullName(@PathVariable String userFullName, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByFullName("%" + userFullName + "%");
        if (users.isEmpty())
            model.addAttribute("message", "No users found for name '" + userFullName + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-location/{userLocation}")
    String getUsersByUserLocation(@PathVariable String userLocation, Model model) {
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
    String getUsersByRoleAndLocation(@RequestParam String userRole, @RequestParam String userLocation, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByRoleAndLocation(userRole, userLocation);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for role '" + userRole + "' and location '" + userLocation + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-cat")
    String getUsersByCatName(@RequestParam String catName, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByCatName(catName);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for cat '" + catName + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/by-search-term/{searchTerm}")
    String getUsersByUserNameOrCatName(@PathVariable String searchTerm, Model model) {
        List<UserOutputDTO> users = userService.getAllUsersByUserNameOrCatName(searchTerm);
        if (users.isEmpty())
            model.addAttribute("message", "No results found for search term '" + searchTerm + "'.");
        model.addAttribute("users", users);
        return "user/user-list";
    }

    // Remaining logic remains unchanged as caching is handled at the service layer
}
