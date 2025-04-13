package org.example.springboot25.security;

import org.aspectj.weaver.ast.Not;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.UserRepository;
import org.example.springboot25.service.UserService;
import org.example.springboot25.utility.RandomUsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
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
            User user = userService.getUserByEmail(email);
            String newFullName = oAuth2User.getAttribute("name");
            if (!newFullName.equals(user.getUserFullName())) {
                user.setUserFullName(newFullName);
            }
            userService.updateUser(user.getUserId(), user);
            logger.info("Updated existing user: {}", email);
        } catch (NotFoundException e) {
            User user = new User();
            user.setUserEmail(email);
            user.setUserFullName(oAuth2User.getAttribute("name"));
            user.setUserName(RandomUsernameGenerator.getRandomUsername());
            user.setUserAuthProvider("google");
            user.setUserRole(UserRole.BASIC);
            userService.addUser(user);
            logger.info("Provisioned new user: {}", email);
        }
        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_BASIC"),
                oAuth2User.getAttributes(),
                "sub"
        );
    }
}
