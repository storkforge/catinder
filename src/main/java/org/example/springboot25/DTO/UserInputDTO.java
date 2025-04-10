package org.example.springboot25.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserInputDTO {

    @NotBlank
    private String userFullName;

    @NotBlank
    private String userName;

    @Email
    @NotBlank
    private String userEmail;

    @NotBlank
    private String userLocation;

    @NotBlank
    private String userRole;

    @NotBlank
    private String userAuthProvider;

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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserAuthProvider() {
        return userAuthProvider;
    }

    public void setUserAuthProvider(String userAuthProvider) {
        this.userAuthProvider = userAuthProvider;
    }
}
