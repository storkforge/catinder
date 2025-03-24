package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @NotBlank
    private String recommendationCategory;

    @NotBlank
    private String recommendationProductName;

    @NotBlank
    private String recommendationProductDescription;

    @NotBlank
    private String recommendationProductLink;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recommendation_cat_id")
    private Cat catRecommendationCat;


    public Long getRecommendationId() {
        return recommendationId;
    }

    public @NotBlank String getRecommendationCategory() {
        return recommendationCategory;
    }

    public void setRecommendationCategory(@NotBlank String recommendationCategory) {
        this.recommendationCategory = recommendationCategory;
    }

    public @NotBlank String getRecommendationProductName() {
        return recommendationProductName;
    }

    public void setRecommendationProductName(@NotBlank String recommendationProductName) {
        this.recommendationProductName = recommendationProductName;
    }

    public @NotBlank String getRecommendationProductDescription() {
        return recommendationProductDescription;
    }

    public void setRecommendationProductDescription(@NotBlank String recommendationProductDescription) {
        this.recommendationProductDescription = recommendationProductDescription;
    }

    public @NotBlank String getRecommendationProductLink() {
        return recommendationProductLink;
    }

    public void setRecommendationProductLink(@NotBlank String recommendationProductLink) {
        this.recommendationProductLink = recommendationProductLink;
    }

    public @NotNull Cat getCatRecommendationCat() {
        return catRecommendationCat;
    }

    public void setCatRecommendationCat(@NotNull Cat catRecommendationCat) {
        this.catRecommendationCat = catRecommendationCat;
    }

}
