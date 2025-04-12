package org.example.springboot25.controller;

import org.example.springboot25.service.AI_RecommendationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-recommendations")
public class AI_RecommendationRestController {

    private final AI_RecommendationService aiRecommendationService;

    public AI_RecommendationRestController(AI_RecommendationService aiRecommendationService) {
        this.aiRecommendationService = aiRecommendationService;
    }

    @GetMapping("/{breed}")
    public String getRecommendation(@PathVariable String breed) {
        return aiRecommendationService.getRecommendationForBreed(breed);
    }
}

