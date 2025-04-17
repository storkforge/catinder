package org.example.springboot25.controller;

import org.example.springboot25.service.CatImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/catimage")
public class CatImageController {

    private final CatImageService catImageService;

    public CatImageController(CatImageService catImageService) {
        this.catImageService = catImageService;
    }

    @GetMapping("/catimage")
    public String getCatImage(Model model) {
        String imageUrl = catImageService.getCatImageUrl();
        model.addAttribute("catImageUrl", imageUrl);
        return "catimage/catimage";
    }
}
