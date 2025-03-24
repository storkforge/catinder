package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    private String userName;

    @Email
    @NotBlank
    private String userEmail;

    @NotBlank
    private String userLocation;

    @NotNull
    private String userRole;

    @NotBlank
    private String userAuthProvider;

    @OneToMany(mappedBy = "userCatOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cat> userCats;

    @OneToMany(mappedBy = "userEventPlanner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> userPlannedEvents;

    @OneToMany(mappedBy = "userEventParticipant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipant> userEventParticipants;

    @OneToMany(mappedBy = "userPostAuthor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> userPost;


    public Long getUserId() {
        return userId;
    }

    public @NotBlank String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank String userName) {
        this.userName = userName;
    }

    public @Email @NotBlank String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(@Email @NotBlank String userEmail) {
        this.userEmail = userEmail;
    }

    public @NotBlank String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(@NotBlank String userLocation) {
        this.userLocation = userLocation;
    }

    public @NotNull String getUserRole() {
        return userRole;
    }

    public void setUserRole(@NotNull String userRole) {
        this.userRole = userRole;
    }

    public @NotBlank String getUserAuthProvider() {
        return userAuthProvider;
    }

    public void setUserAuthProvider(@NotBlank String userAuthProvider) {
        this.userAuthProvider = userAuthProvider;
    }

    public List<Cat> getUserCats() {
        return userCats;
    }

    public void setUserCats(List<Cat> userCats) {
        this.userCats = userCats;
    }

    public List<Event> getUserPlannedEvents() {
        return userPlannedEvents;
    }

    public void setUserPlannedEvents(List<Event> userPlannedEvents) {
        this.userPlannedEvents = userPlannedEvents;
    }

    public List<EventParticipant> getUserEventParticipants() {
        return userEventParticipants;
    }

    public void setUserEventParticipants(List<EventParticipant> userEventParticipants) {
        this.userEventParticipants = userEventParticipants;
    }

    public List<Post> getUserPost() {
        return userPost;
    }

    public void setUserPost(List<Post> userPost) {
        this.userPost = userPost;
    }

}
