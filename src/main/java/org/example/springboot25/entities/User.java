package org.example.springboot25.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id; // Redis ID
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@RedisHash("User")
public class User implements Serializable {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @jakarta.persistence.Id
    @Id // Redis ID
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

    private String userLocation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @NotBlank
    private String userAuthProvider;

    @Column(nullable = true)
    private String userPassword;

    @JsonIgnore
    @OneToMany(mappedBy = "userCatOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cat> userCats = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userEventPlanner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> userPlannedEvents = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EventParticipant> userEventParticipants = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userPostAuthor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> userPost = new ArrayList<>();

    // ======================
    // Getters and Setters
    // ======================

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
        if (userName == null) {
            this.userName = null;
        } else if (!userName.startsWith("@")) {
            this.userName = "@" + userName;
        } else {
            this.userName = userName;
        }
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

    public String getUserPassword() {
        return null; // never expose it directly
    }

    public void setUserPassword(String userPassword) {
        if (userPassword != null && !userPassword.isEmpty()) {
            this.userPassword = ENCODER.encode(userPassword);
        } else {
            this.userPassword = null;
        }
    }

    public boolean checkPassword(String rawPassword) {
        return ENCODER.matches(rawPassword, this.userPassword);
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
