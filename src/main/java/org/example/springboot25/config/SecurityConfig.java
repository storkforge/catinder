package org.example.springboot25.config;

import org.example.springboot25.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Value("${remember.me.key:myLongTermRememberMeKey}")
    private String rememberMeKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/premium/**").hasRole("PREMIUM")
                        .requestMatchers("/user/**").hasAnyRole("BASIC", "PREMIUM")
                        .requestMatchers("/", "/register", "/css/**", "/js/**", "/images/**", "/error/**").permitAll()
                        .anyRequest().authenticated()
                )
                .rememberMe(remember -> remember
                        .key(rememberMeKey)
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 1 vecka
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Redirect to homepage after logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((_req, res, auth) -> {
                            boolean isAdmin = auth.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                            res.sendRedirect(isAdmin ? "/admin/" : "/");
                        })
                )
                .build();
    }
}
