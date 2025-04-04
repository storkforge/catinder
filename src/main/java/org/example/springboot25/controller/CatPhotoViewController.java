package org.example.springboot25.controller;

import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.service.CatPhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller  // 🔹 Detta gör att Spring vet att den ska hantera HTML-sidor
public class CatPhotoViewController {

    private final CatPhotoService catPhotoService;

    public CatPhotoViewController(CatPhotoService catPhotoService) {
        this.catPhotoService = catPhotoService;
    }

    // 🔹 Visa en HTML-sida med alla kattbilder
    @GetMapping("/view/catphotos")
    public String showCatPhotos(Model model) {
        List<CatPhoto> catPhotos = catPhotoService.getAllCatPhotos();
        model.addAttribute("catPhotos", catPhotos);
        return "catphotos";  // 🔹 Returnerar en HTML-sida som heter "catphotos.html"
    }
}

