package org.example.springboot25.service;

import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.exceptions.RecommendationNotFoundException;
import org.example.springboot25.repository.RecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Transactional
    public List<Recommendation> getAllRecommendations() {
        return recommendationRepository.findAll();
    }

    @Transactional
    public Recommendation getRecommendationById(Long id) {
        return recommendationRepository.findById(id)
                .orElseThrow(() -> new RecommendationNotFoundException("Rekommendation med id " + id + " hittades inte"));
    }

    @Transactional
    public Recommendation createRecommendation(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

    @Transactional
    public void deleteRecommendation(Long id) {
        if (!recommendationRepository.existsById(id)) {
            throw new RecommendationNotFoundException("Rekommendation med id " + id + " hittades inte");
        }
        recommendationRepository.deleteById(id);
    }

    @Transactional
    public Recommendation updateRecommendation(Long id, Recommendation updated) {
        Recommendation existing = recommendationRepository.findById(id)
                .orElseThrow(() -> new RecommendationNotFoundException("Rekommendation med id " + id + " hittades inte"));

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

        // Om ni vill tillåta att byta katt också (men oftast inte i en PATCH)
        if (updated.getCatRecommendationCat() != null) {
            existing.setCatRecommendationCat(updated.getCatRecommendationCat());
        }

        return recommendationRepository.save(existing);
    }

    @Transactional
    public Recommendation patchRecommendation(Long id, Map<String, Object> updates) {
        Recommendation existing = recommendationRepository.findById(id)
                .orElseThrow(() -> new RecommendationNotFoundException("Rekommendation med id " + id + " hittades inte"));

        if (updates.get("recommendationCategory") instanceof String cat) {
            existing.setRecommendationCategory(cat);
        }
        if (updates.get("recommendationProductName") instanceof String name) {
            existing.setRecommendationProductName(name);
        }
        if (updates.get("recommendationProductDescription") instanceof String desc) {
            existing.setRecommendationProductDescription(desc);
        }
        if (updates.get("recommendationProductLink") instanceof String link) {
            existing.setRecommendationProductLink(link);
        }

        boolean updatesApplied = updates.containsKey("recommendationCategory")
                || updates.containsKey("recommendationProductName")
                || updates.containsKey("recommendationProductDescription")
                || updates.containsKey("recommendationProductLink");

        if (!updatesApplied) {
            return existing;
        }

        return recommendationRepository.save(existing);
    }
}