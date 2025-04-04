package org.example.springboot25.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    private String userFullName;

    @NotBlank
    @Column(unique = true)
    private String userName;

    @Email
    @NotBlank
    @Column(unique = true)
    private String userEmail;

    @NotBlank
    private String userLocation;

    @NotNull
    private String userRole;

    @NotBlank
    private String userAuthProvider;

    @NotBlank
    private String password;

    @OneToMany(mappedBy = "userCatOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cat> userCats = new ArrayList<>();

    @OneToMany(mappedBy = "userEventPlanner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> userPlannedEvents = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EventParticipant> userEventParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "userPostAuthor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> userPost = new ArrayList<>();


    public Long getUserId() {
        return userId;
    }

    public @NotBlank String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String fullName) {
        this.userFullName = fullName;
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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
