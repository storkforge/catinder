package org.example.springboot25.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.springboot25.entities.UserRole;

/**
 * DTO for capturing user input when creating a new user.
 */
public class UserInputDTO {

    @NotBlank(message = "Full name is required.")
    private String userFullName;

    @NotBlank(message = "Username is required.")
    private String userName;

    @Email(message = "Email must be valid.")
    @NotBlank(message = "Email is required.")
    private String userEmail;

    @NotBlank(message = "Location is required.")
    private String userLocation;

    @NotBlank(message = "User role is required.")
    private UserRole userRole;

    @NotBlank(message = "Auth provider is required.")
    private String userAuthProvider;

    // Getters and Setters
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
