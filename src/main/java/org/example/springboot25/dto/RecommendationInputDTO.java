package org.example.springboot25.dto;

public class RecommendationInputDTO {

    private String recommendationCategory;
    private String recommendationProductName;
    private String recommendationProductDescription;
    private String recommendationProductLink;
    private Long userId;
    private Long catId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }
}
