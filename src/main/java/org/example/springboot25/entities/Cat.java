package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catId;

    @NotBlank
    private String catName;

    private String catProfilePicture;

    private String catBreed;

    private String catGender;

    @Min(0)
    private int catAge;

    private String catPersonality;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cat_owner_user_id")
    private User userCatOwner;

    @OneToMany(mappedBy = "catPhotoCat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatPhoto> catPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "catReminderCat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> catReminders = new ArrayList<>();

    @OneToMany(mappedBy = "catRecommendationCat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> catRecommendations = new ArrayList<>();

    public Long getCatId() {
        return catId;
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

    public int getCatAge() {
        return catAge;
    }

    public void setCatAge(int catAge) {
        this.catAge = catAge;
    }

    public String getCatPersonality() {
        return catPersonality;
    }

    public void setCatPersonality(String catPersonality) {
        this.catPersonality = catPersonality;
    }

    public User getUserCatOwner() {
        return userCatOwner;
    }

    public void setUserCatOwner(User userCatOwner) {
        this.userCatOwner = userCatOwner;
    }

    public List<CatPhoto> getCatPhotos() {
        return catPhotos;
    }

    public void setCatPhotos(List<CatPhoto> catPhotos) {
        this.catPhotos = catPhotos;
    }

    public List<Reminder> getCatReminders() {
        return catReminders;
    }

    public void setCatReminders(List<Reminder> catReminders) {
        this.catReminders = catReminders;
    }

    public List<Recommendation> getCatRecommendations() {
        return catRecommendations;
    }

    public void setCatRecommendations(List<Recommendation> catRecommendations) {
        this.catRecommendations = catRecommendations;
    }

}
