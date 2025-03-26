package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Recommendation> getAllRecommendations() {
        return recommendationService.getAllRecommendations();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recommendation getRecommendationById(@PathVariable Long id) {
        return recommendationService.getRecommendationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Recommendation createRecommendation(@RequestBody @Valid Recommendation recommendation) {
        return recommendationService.createRecommendation(recommendation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recommendation updateRecommendation(@PathVariable Long id, @RequestBody @Valid Recommendation recommendation) {
        return recommendationService.updateRecommendation(id, recommendation);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recommendation patchRecommendation(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return recommendationService.patchRecommendation(id, updates);
    }
}
