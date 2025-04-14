package org.example.springboot25.dto;

import java.util.Objects;

public class CatPhotoUpdateDTO {

    private String catPhotoUrl;
    private String catPhotoCaption;
    private Long catPhotoCatId;

    public CatPhotoUpdateDTO() {}


    public CatPhotoUpdateDTO(String catPhotoUrl, String catPhotoCaption, Long catPhotoCatId) {
        this.catPhotoUrl = catPhotoUrl;
        this.catPhotoCaption = catPhotoCaption;
        this.catPhotoCatId = catPhotoCatId;
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
        CatPhotoUpdateDTO that = (CatPhotoUpdateDTO) o;
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
        return "CatPhotoUpdateDTO{" +
                "catPhotoCaption='" + catPhotoCaption + '\'' +
                ", catPhotoUrl='" + catPhotoUrl + '\'' +
                ", catPhotoCatId=" + catPhotoCatId +
                '}';
    }

}
