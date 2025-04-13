package org.example.springboot25.controller.controllerAdvice;

import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final UserService userService;

    @Autowired
    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof DefaultOAuth2User) {
                DefaultOAuth2User oauthUser = (DefaultOAuth2User) principal;
                String email = oauthUser.getAttribute("email");
                if (email != null) {
                    try {
                        return userService.getUserByEmail(email);
                    } catch (NotFoundException e) {
                        // Optionally handle not found, return null or fallback
                    }
                }
            }
        }
        return null;
    }
}
