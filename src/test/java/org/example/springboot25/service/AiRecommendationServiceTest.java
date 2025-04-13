package org.example.springboot25.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class AiRecommendationServiceTest {

    private AiRecommendationService aiRecommendationService;

    @BeforeEach
    void setUp() {
        aiRecommendationService = new AiRecommendationService();
    }

    @Test
    void shouldReturnCorrectRecommendationForValidBreed() {
        String result = aiRecommendationService.getRecommendationForBreed("PERSIAN");
        assertThat(result).contains("Groom daily");
    }

    @Test
    void shouldReturnCorrectRecommendationForBreedWithSpaces() {
        String result = aiRecommendationService.getRecommendationForBreed("american shorthair");
        assertThat(result).contains("Easy-going");
    }

    @Test
    void shouldReturnCorrectRecommendationForLowercaseInput() {
        String result = aiRecommendationService.getRecommendationForBreed("siamese");
        assertThat(result).contains("Talkative");
    }

    @Test
    void shouldHandleUnknownBreedGracefully() {
        String result = aiRecommendationService.getRecommendationForBreed("fluffybean");
        assertThat(result).contains("Unknown breed");
    }

    @Test
    void shouldHandleEmptyStringInput() {
        String result = aiRecommendationService.getRecommendationForBreed("");
        assertThat(result).contains("Unknown breed");
    }

    @Test
    void shouldHandleWhitespaceOnlyInput() {
        String result = aiRecommendationService.getRecommendationForBreed("   ");
        assertThat(result).contains("Unknown breed");
    }

    @Test
    void shouldHandleNullInput() {
        String result = aiRecommendationService.getRecommendationForBreed(null);
        assertThat(result).contains("Unknown breed");
    }
}

