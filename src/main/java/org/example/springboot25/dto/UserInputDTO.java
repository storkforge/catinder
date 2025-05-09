package org.example.springboot25.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import org.example.springboot25.entities.UserRole;

public class UserInputDTO {


    private Long userId;

    @NotBlank
    private String userFullName;

    @NotBlank
    private String userName;

    @Email
    @NotBlank
    private String userEmail;

    @NotBlank
    private String userLocation;

    @NotNull
    private UserRole userRole;

    @NotBlank
    private String userAuthProvider;

    public UserInputDTO() {}

    public UserInputDTO(String userFullName, String userName, String userEmail, String userLocation,
                        UserRole userRole, String userAuthProvider) {
        this.userFullName = userFullName;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userLocation = userLocation;
        this.userRole = userRole;
        this.userAuthProvider = userAuthProvider;
    }

    // Getters and Setters
    public Long getUserId() {return userId;}

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUserAuthProvider() {
        return userAuthProvider;
    }

    public void setUserAuthProvider(String userAuthProvider) {
        this.userAuthProvider = userAuthProvider;
    }

}
