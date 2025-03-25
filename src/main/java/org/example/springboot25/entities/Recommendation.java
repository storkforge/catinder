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

    public String getRecommendationCategory() {
        return recommendationCategory;
    }

    public void setRecommendationCategory(String recommendationCategory) {
        this.recommendationCategory = recommendationCategory;
    }

    public String getRecommendationProductName() {
        return recommendationProductName;
    }

    public void setRecommendationProductName(String recommendationProductName) {
        this.recommendationProductName = recommendationProductName;
    }

    public String getRecommendationProductDescription() {
        return recommendationProductDescription;
    }

    public void setRecommendationProductDescription(String recommendationProductDescription) {
        this.recommendationProductDescription = recommendationProductDescription;
    }

    public String getRecommendationProductLink() {
        return recommendationProductLink;
    }

    public void setRecommendationProductLink(String recommendationProductLink) {
        this.recommendationProductLink = recommendationProductLink;
    }

    public Cat getCatRecommendationCat() {
        return catRecommendationCat;
    }

    public void setCatRecommendationCat(Cat catRecommendationCat) {
        this.catRecommendationCat = catRecommendationCat;
    }

}
