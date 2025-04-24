package org.example.springboot25.security;

import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.UserService;
import org.example.springboot25.utility.RandomUsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final UserService userService;

    @Autowired
    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            logger.error("Email not found from OAuth2 provider");
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        try {
            // Check for existing user
            User user = userService.findUserByEmail(email);
            String newFullName = oAuth2User.getAttribute("name");

            // Only update if name has changed
            if (newFullName != null && !newFullName.equals(user.getUserFullName())) {
                UserUpdateDTO updateDTO = new UserUpdateDTO();
                updateDTO.setUserFullName(newFullName);
                updateDTO.setUserId(user.getUserId()); // Required for proper cache eviction/update
                userService.updateUser(user.getUserId(), updateDTO);
                logger.info("Updated existing user: {}", email);
            }

        } catch (NotFoundException e) {
            // Create new user if not found
            User user = new User();
            user.setUserEmail(email);

            String fullName = oAuth2User.getAttribute("name");
            user.setUserFullName(fullName != null ? fullName : "");
            user.setUserName(RandomUsernameGenerator.getRandomUsername());

            String providerName = userRequest.getClientRegistration().getRegistrationId();
            user.setUserAuthProvider(providerName);
            user.setUserRole(UserRole.BASIC);

            userService.addUser(user); // This will automatically cache new user
            logger.info("Provisioned new user: {}", email);
        }

        // Fetch final user for login session creation
        User finalUser = userService.findUserByEmail(email);

        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_" + finalUser.getUserRole().name()),
                oAuth2User.getAttributes(),
                "sub"
        );
    }
}
