package org.example.springboot25.config;

import org.example.springboot25.security.CustomUserDetails;
import org.example.springboot25.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userDetailsService;
    }

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/premium/**").hasRole("PREMIUM")
                        .requestMatchers("/user/**").hasAnyRole("BASIC", "PREMIUM")
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout")
                        .permitAll()
                )
                .oauth2Login(OAuth2LoginConfigurer::defaultSuccessUrl); // för google/facebook

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}
