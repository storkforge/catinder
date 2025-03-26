package org.example.springboot25.service;

import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.exceptions.RecommendationNotFoundException;
import org.example.springboot25.repository.RecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}