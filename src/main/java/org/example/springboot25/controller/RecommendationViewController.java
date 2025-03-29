package org.example.springboot25.controller;

import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.service.RecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecommendationViewController {

    private final RecommendationService recommendationService;

    public RecommendationViewController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations")
    public String showRecommendations(Model model) {
        List<Recommendation> recommendations = recommendationService.getAllRecommendations();
        model.addAttribute("recommendations", recommendations);
        return "recommendations";
    }
}
