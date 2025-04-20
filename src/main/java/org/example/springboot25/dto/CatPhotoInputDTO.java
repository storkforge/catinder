package org.example.springboot25.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CatPhotoInputDTO {

    @NotNull
    private Long catPhotoCatId;

    @NotBlank
    private String catPhotoUrl;

    @NotBlank
    private String catPhotoCaption;

    public CatPhotoInputDTO() {}

    public CatPhotoInputDTO(Long catPhotoCatId, String catPhotoUrl, String catPhotoCaption) {
        this.catPhotoCatId = catPhotoCatId;
        this.catPhotoUrl = catPhotoUrl;
        this.catPhotoCaption = catPhotoCaption;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatPhotoInputDTO that = (CatPhotoInputDTO) o;
        return Objects.equals(catPhotoCaption, that.catPhotoCaption) &&
                Objects.equals(catPhotoUrl, that.catPhotoUrl) &&
                Objects.equals(catPhotoCatId, that.catPhotoCatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catPhotoCaption, catPhotoUrl, catPhotoCatId);
    }

    @Override
    public String toString() {
        return "CatPhotoInputDTO{" +
                "catPhotoCaption='" + catPhotoCaption + '\'' +
                ", catPhotoUrl='" + catPhotoUrl + '\'' +
                ", catPhotoCatId=" + catPhotoCatId +
                '}';
    }

}
