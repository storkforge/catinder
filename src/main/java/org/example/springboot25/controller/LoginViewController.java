package org.example.springboot25.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginViewController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

}
