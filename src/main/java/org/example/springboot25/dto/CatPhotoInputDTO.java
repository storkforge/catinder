package org.example.springboot25.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CatPhotoInputDTO {

    @NotNull
    private Long catPhotoCatId;

    @NotBlank
    private String catPhotoUrl;

    private String catPhotoCaption;

    public String getCatPhotoUrl() {
        return catPhotoUrl;
    }

    public void setCatPhotoUrl(String catPhotoUrl) {
        this.catPhotoUrl = catPhotoUrl;
    }

    public String getCatPhotoCaption() {
        return catPhotoCaption;
    }

    public void setCatPhotoCaption(String catPhotoCaption) {
        this.catPhotoCaption = catPhotoCaption;
    }

    public Long getCatPhotoCatId() {
        return catPhotoCatId;
    }

    public void setCatPhotoCatId(Long catPhotoCatId) {
        this.catPhotoCatId = catPhotoCatId;
    }
}
