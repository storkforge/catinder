package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

@Entity
public class CatPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catPhotoId;

    @NotBlank
    private String catPhotoUrl;

    private String catPhotoCaption;

    @PastOrPresent
    private LocalDateTime catPhotoCreatedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cat_photo_cat_id")
    private Cat catPhotoCat;


    public Long getCatPhotoId() {
        return catPhotoId;
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

    public LocalDateTime getCatPhotoCreatedAt() {
        return catPhotoCreatedAt;
    }

    public void setCatPhotoCreatedAt(LocalDateTime catPhotoCreatedAt) {
        this.catPhotoCreatedAt = catPhotoCreatedAt;
    }

    public Cat getCatPhotoCat() {
        return catPhotoCat;
    }

    public void setCatPhotoCat(Cat catPhotoCat) {
        this.catPhotoCat = catPhotoCat;
    }

}
