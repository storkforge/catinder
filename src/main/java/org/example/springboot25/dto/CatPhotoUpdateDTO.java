package org.example.springboot25.dto;

public class CatPhotoUpdateDTO {

    private String catPhotoUrl;
    private String catPhotoCaption;
    private Long catPhotoCatId;

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
