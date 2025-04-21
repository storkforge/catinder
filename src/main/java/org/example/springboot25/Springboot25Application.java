package org.example.springboot25;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableCaching
@EnableJpaRepositories(basePackages = "org.example.springboot25.repository")
public class Springboot25Application {

    public static void main(String[] args) {
        SpringApplication.run(Springboot25Application.class, args);
    }
}
