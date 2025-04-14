package org.example.springboot25.mapper;

import org.example.springboot25.dto.RecommendationInputDTO;
import org.example.springboot25.dto.RecommendationOutputDTO;
import org.example.springboot25.dto.RecommendationUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

@Component
public class RecommendationMapper {

    public Recommendation toEntityInput(RecommendationInputDTO input, User user, Cat cat) {
        Recommendation recommendation = new Recommendation();
        recommendation.setRecommendationCategory(input.getRecommendationCategory());
        recommendation.setRecommendationProductName(input.getRecommendationProductName());
        recommendation.setRecommendationProductDescription(input.getRecommendationProductDescription());
        recommendation.setRecommendationProductLink(input.getRecommendationProductLink());
        recommendation.setUser(user);
        recommendation.setCatRecommendationCat(cat);
        return recommendation;
    }

    public RecommendationOutputDTO toDTO(Recommendation recommendation) {
        RecommendationOutputDTO dto = new RecommendationOutputDTO();
        dto.setId(recommendation.getRecommendationId());
        dto.setRecommendationCategory(recommendation.getRecommendationCategory());
        dto.setRecommendationProductName(recommendation.getRecommendationProductName());
        dto.setRecommendationProductDescription(recommendation.getRecommendationProductDescription());
        dto.setRecommendationProductLink(recommendation.getRecommendationProductLink());
        dto.setUserId(recommendation.getUser() != null ? recommendation.getUser().getUserId() : null);
        dto.setCatId(recommendation.getCatRecommendationCat() != null ? recommendation.getCatRecommendationCat().getCatId() : null);
        return dto;
    }

    public Recommendation toEntityUpdate(RecommendationUpdateDTO input, Recommendation existingRecommendation, User user, Cat cat) {
        if (input.getRecommendationCategory() != null) {
            existingRecommendation.setRecommendationCategory(input.getRecommendationCategory());
        }
        if (input.getRecommendationProductName() != null) {
            existingRecommendation.setRecommendationProductName(input.getRecommendationProductName());
        }
        if (input.getRecommendationProductDescription() != null) {
            existingRecommendation.setRecommendationProductDescription(input.getRecommendationProductDescription());
        }
        if (input.getRecommendationProductLink() != null) {
            existingRecommendation.setRecommendationProductLink(input.getRecommendationProductLink());
        }

        if (user != null) {
            existingRecommendation.setUser(user);
        }
        if (cat != null) {
            existingRecommendation.setCatRecommendationCat(cat);
        }
        return existingRecommendation;
    }
}

