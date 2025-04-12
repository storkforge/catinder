package org.example.springboot25.controller;

import org.example.springboot25.service.AI_RecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ai-recommendations")
public class AI_RecommendationViewController {

    private final AI_RecommendationService aiRecommendationService;

    public AI_RecommendationViewController(AI_RecommendationService aiRecommendationService) {
        this.aiRecommendationService = aiRecommendationService;
    }

    @GetMapping
    public String showForm() {
        return "ai_recommendation_form";
    }

    @PostMapping
    public String showResult(@RequestParam String breed, Model model) {
        String recommendation = aiRecommendationService.getRecommendationForBreed(breed);
        model.addAttribute("breed", breed);
        model.addAttribute("recommendation", recommendation);
        return "ai_recommendation_result";
    }
}
