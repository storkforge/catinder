package org.example.springboot25.controller;

import org.example.springboot25.service.AboutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class AboutController {

    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        try {
            String htmlContent = aboutService.renderMarkdownFile();
            model.addAttribute("readmeContent", htmlContent);
        } catch (IOException e) {
            model.addAttribute("readmeContent", "Unable to load README file.");
        }
        return "about";
    }
}
