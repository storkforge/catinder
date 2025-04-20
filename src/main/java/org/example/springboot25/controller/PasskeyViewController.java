package org.example.springboot25.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/passkey")
public class PasskeyViewController {

    @GetMapping("/demo")
    public String showPasskeyDemoPage() {
        return "passkey/passkey-demo";
    }
}
