package org.example.springboot25.dto;

import org.example.springboot25.entities.UserRole;

/**
 * DTO for exposing user details externally without sensitive information.
 */
public class UserOutputDTO {

    private Long userId;
    private String userFullName;
    private String userName;
    private String userEmail;
    private String userLocation;
    private UserRole userRole;
    private String userAuthProvider;

    public UserOutputDTO() {}

    public UserOutputDTO(Long userId, String userFullName, String userName, String userEmail,
                         String userLocation, UserRole userRole, String userAuthProvider) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userLocation = userLocation;
        this.userRole = userRole;
        this.userAuthProvider = userAuthProvider;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
