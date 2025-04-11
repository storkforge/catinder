package org.example.springboot25;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class Springboot25Application {

    public static void main(String[] args) {
        SpringApplication.run(Springboot25Application.class, args);
    }

}
