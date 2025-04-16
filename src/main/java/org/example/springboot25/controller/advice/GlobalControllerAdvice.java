package org.example.springboot25.controller.advice;

import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);
    private final UserService userService;

    @Autowired
    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser(Authentication authentication) {
        log.trace("Resolving currentUser from Authentication: {}",
                authentication != null ? authentication.getName() : "null");
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            log.debug("Authentication principal class: {}", principal.getClass().getName());
            if (principal instanceof DefaultOAuth2User oauthUser) {
                String email = oauthUser.getAttribute("email");
                log.debug("OAuth2 email attribute: {}", email);
                if (email != null) {
                    try {
                        return userService.getUserByEmail(email);
                    } catch (NotFoundException e) {
                        log.error(e.getMessage());
                        log.warn("User not found for email {} from OAuth2 login", email, e);
                    }
                }
            } else if (principal instanceof String username) {
                try {
                    return userService.getUserByUserName(username);
                } catch (NotFoundException e) {
                    log.error(e.getMessage());
                }
            } else if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
                try {
                    return userService.getUserByUserName(userDetails.getUsername());
                } catch (NotFoundException e) {
                    log.error(e.getMessage());
                }
            }
        }
        log.trace("No currentUser resolved");
        return null;
    }
}
