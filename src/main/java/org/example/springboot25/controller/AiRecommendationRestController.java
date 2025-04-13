package org.example.springboot25.controller;

import org.example.springboot25.service.AiRecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-recommendations")
public class AiRecommendationRestController {

    private final AiRecommendationService aiRecommendationService;

    public AiRecommendationRestController(AiRecommendationService aiRecommendationService) {
        this.aiRecommendationService = aiRecommendationService;
    }

    // Return recommendation based on cat breed with structured JSON and error handling
    @GetMapping("/{breed}")
    public ResponseEntity<?> getRecommendation(@PathVariable String breed) {
        String recommendation = aiRecommendationService.getRecommendationForBreed(breed);

        // If the breed is not recognized, return error message with 404
        if (recommendation.contains("Unknown breed")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unknown cat breed");
            errorResponse.put("message", recommendation);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // Success: return breed and recommendation
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("breed", breed);
        successResponse.put("recommendation", recommendation);
        return ResponseEntity.ok(successResponse);
    }
}
