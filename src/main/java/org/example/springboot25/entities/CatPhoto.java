package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class CatPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catPhotoId;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String catPhotoUrl;

    @Column(length = 1000)
    private String catPhotoCaption;

    @PastOrPresent
    @CreationTimestamp
    private LocalDateTime catPhotoCreatedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cat_photo_cat_id")
    private Cat catPhotoCat;

    public CatPhoto() {}

    public CatPhoto(String url, String caption, Cat cat) {
        if (url == null || url.isBlank()){
            throw new IllegalArgumentException("URL cannot be null or blank");
        }
        if (cat == null){
            throw new IllegalArgumentException("Cat cannot be null");
        }
        this.catPhotoUrl = url;
        this.catPhotoCaption = caption;
        this.catPhotoCat = cat;
    }

    public Long getCatPhotoId() {
        return catPhotoId;
    }

    /*public void setCatPhotoId(Long catPhotoId) {
        this.catPhotoId = catPhotoId;
    }*/

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

    /*public void setCatPhotoCreatedAt(LocalDateTime catPhotoCreatedAt) {
        this.catPhotoCreatedAt = catPhotoCreatedAt;
    }*/

    public Cat getCatPhotoCat() {
        return catPhotoCat;
    }

    public void setCatPhotoCat(Cat catPhotoCat) {
        this.catPhotoCat = catPhotoCat;
    }

}
