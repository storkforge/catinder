package org.example.springboot25.controller;

import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.service.CatPhotoService;
import org.example.springboot25.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    private final CatPhotoService catPhotoService;

    public HomeController(CatPhotoService catPhotoService) {
        this.catPhotoService = catPhotoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<CatPhoto> catPhotos = catPhotoService.getAllCatPhotos();
        Collections.shuffle(catPhotos);
        model.addAttribute("catPhotos", catPhotos);
        return "index";
    }
}
