package org.example.springboot25.service;

import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.RecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional // Applies to all methods in the class
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public List<Recommendation> getAllRecommendations() {
        return recommendationRepository.findAll();
    }

    public Recommendation getRecommendationById(Long id) {
        return recommendationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recommendation with id " + id + " was not found"));
    }

    public Recommendation createRecommendation(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

    public void deleteRecommendation(Long id) {
        if (!recommendationRepository.existsById(id)) {
            throw new NotFoundException("Recommendation with id " + id + " was not found");
        }
        recommendationRepository.deleteById(id);
    }

    public Recommendation updateRecommendation(Long id, Recommendation updated) {
        Recommendation existing = recommendationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recommendation with id " + id + " was not found"));

        if (updated.getRecommendationCategory() != null) {
            existing.setRecommendationCategory(updated.getRecommendationCategory());
        }

        if (updated.getRecommendationProductName() != null) {
            existing.setRecommendationProductName(updated.getRecommendationProductName());
        }

        if (updated.getRecommendationProductDescription() != null) {
            existing.setRecommendationProductDescription(updated.getRecommendationProductDescription());
        }

        if (updated.getRecommendationProductLink() != null) {
            existing.setRecommendationProductLink(updated.getRecommendationProductLink());
        }

        if (updated.getCatRecommendationCat() != null) {
            existing.setCatRecommendationCat(updated.getCatRecommendationCat());
        }

        return recommendationRepository.save(existing);
    }

    public Recommendation patchRecommendation(Long id, Map<String, Object> updates) {
        Recommendation existing = recommendationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recommendation with id " + id + " was not found"));

        if (updates.get("recommendationCategory") instanceof String category) {
            existing.setRecommendationCategory(category);
        }
        if (updates.get("recommendationProductName") instanceof String name) {
            existing.setRecommendationProductName(name);
        }
        if (updates.get("recommendationProductDescription") instanceof String description) {
            existing.setRecommendationProductDescription(description);
        }
        if (updates.get("recommendationProductLink") instanceof String link) {
            existing.setRecommendationProductLink(link);
        }

        boolean updatesApplied = updates.containsKey("recommendationCategory")
                || updates.containsKey("recommendationProductName")
                || updates.containsKey("recommendationProductDescription")
                || updates.containsKey("recommendationProductLink");

        return updatesApplied ? recommendationRepository.save(existing) : existing;
    }
}