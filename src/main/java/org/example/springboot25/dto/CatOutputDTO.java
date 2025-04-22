package org.example.springboot25.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import org.example.springboot25.entities.CatPhoto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CatOutputDTO implements Serializable {
    private Long catId;
    private String catName;
    private String catProfilePicture;
    private String catBreed;
    private String catGender;
    private Integer catAge;
    private String catPersonality;
    private Long userId;

    @OneToMany(mappedBy = "catPhotoCat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatPhoto> catPhotos = new ArrayList<>();

    public List<CatPhoto> getCatPhotos() {
        return catPhotos;
    }

    public void setCatPhotos(List<CatPhoto> catPhotos) {
        this.catPhotos = catPhotos;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatProfilePicture() {
        return catProfilePicture;
    }

    public void setCatProfilePicture(String catProfilePicture) {
        this.catProfilePicture = catProfilePicture;
    }

    public String getCatBreed() {
        return catBreed;
    }

    public void setCatBreed(String catBreed) {
        this.catBreed = catBreed;
    }

    public String getCatGender() {
        return catGender;
    }

    public void setCatGender(String catGender) {
        this.catGender = catGender;
    }

    public Integer getCatAge() {
        return catAge;
    }

    public void setCatAge(Integer catAge) {
        this.catAge = catAge;
    }

    public String getCatPersonality() {
        return catPersonality;
    }

    public void setCatPersonality(String catPersonality) {
        this.catPersonality = catPersonality;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
