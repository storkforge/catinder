package org.example.springboot25.service;

import jakarta.transaction.Transactional;
import org.example.springboot25.entities.CatBreed;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AI_RecommendationService {

    public String getRecommendationForBreed(String breedName) {
        try {
            CatBreed breed = CatBreed.valueOf(breedName.toUpperCase());

            return switch (breed) {
                case PERSIAN -> "Groom daily and keep indoors. Loves cozy places.";
                case MAINE_COON -> "Needs climbing structures and interactive play.";
                case SIAMESE -> "Talkative! Needs lots of attention and toys.";
                case RAGDOLL -> "Very chill. Enjoys soft beds and cuddles.";
                case BENGAL -> "Super energetic. Needs stimulation and high spaces.";
                case SPHYNX -> "No fur, so keep warm. Needs weekly baths.";
                case BRITISH_SHORTHAIR -> "Calm and independent. Likes puzzle feeders.";
                case SCOTTISH_FOLD -> "Needs gentle handling and soft toys.";
                case ABYSSINIAN -> "Incredibly active. Needs high shelves and games.";
                case AMERICAN_SHORTHAIR -> "Easy-going. Enjoys feather toys and companionship.";
            };

        } catch (IllegalArgumentException e) {
            return "Unknown breed. Please enter one of the top 10 popular breeds.";
        }
    }
}
