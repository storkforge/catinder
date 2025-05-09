package org.example.springboot25.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

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

    //@NotBlank
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

    public User () {}

    public User(Long userId, String fullName, String userName, String email, String location, UserRole role, String authProvider) {
        this.userId = userId;
        this.userFullName = fullName;
        this.userName = userName;
        this.userEmail = email;
        this.userLocation = location;
        this.userRole = role;
        this.userAuthProvider = authProvider;
    }


    public Long getUserId() {
        return userId;
    }

    public @NotBlank String getUserFullName() {
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
        return null; // To not expose even the hashed password
    }

    public void setUserPassword(String userPassword) {
        if (userPassword != null && !userPassword.isEmpty()) {
            this.userPassword = ENCODER.encode(userPassword);
        } else {
            this.userPassword = null;
        }
    }

    /**
     * Verifies if the provided raw password matches the stored hashed password
     *
     * @param rawPassword the password to check
     * @return true if the password matches, false otherwise
     */
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

    public void setUserId(Long userId) { this.userId = userId; }
}
