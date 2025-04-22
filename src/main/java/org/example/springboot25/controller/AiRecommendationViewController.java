package org.example.springboot25.controller;

import org.example.springboot25.service.AiRecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ai-recommendations")
public class AiRecommendationViewController {

    private final AiRecommendationService aiRecommendationService;

    public AiRecommendationViewController(AiRecommendationService aiRecommendationService) {
        this.aiRecommendationService = aiRecommendationService;
    }

    // Displays the form where the user can input the cat breed
    @GetMapping
    public String showForm() {
        return "ai_recommendation_form";
    }

    @PostMapping
    public String showResult(@RequestParam String breed, Model model) {
        // Validate input: ensure the breed is not null or empty
        if (breed == null || breed.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a breed name");
            return "ai_recommendation_form";
        }

        breed = breed.trim();
        String recommendation = aiRecommendationService.getRecommendationForBreed(breed);
        model.addAttribute("breed", breed);
        model.addAttribute("recommendation", recommendation);
        return "ai_recommendation_result";
    }
}
