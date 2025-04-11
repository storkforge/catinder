package org.example.springboot25.DTO;

public class CatOutputDTO {
    private Long catId;
    private String catName;
    private String catProfilePicture;
    private String catBreed;
    private String catGender;
    private Integer catAge;
    private String catPersonality;

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
}
