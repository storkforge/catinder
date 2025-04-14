package org.example.springboot25.dto;

import java.time.OffsetDateTime;

public class CatPhotoOutputDTO {

    private Long id;
    private String catPhotoUrl;
    private String catPhotoCaption;
    private OffsetDateTime catPhotoCreatedAt;
    private Long catPhotoCatId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OffsetDateTime getCatPhotoCreatedAt() {
        return catPhotoCreatedAt;
    }

    public void setCatPhotoCreatedAt(OffsetDateTime catPhotoCreatedAt) {
        this.catPhotoCreatedAt = catPhotoCreatedAt;
    }

    public Long getCatPhotoCatId() {
        return catPhotoCatId;
    }

    public void setCatPhotoCatId(Long catPhotoCatId) {
        this.catPhotoCatId = catPhotoCatId;
    }
}
