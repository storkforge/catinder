package org.example.springboot25.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Setter
    @Getter
    @NotBlank
    private String userFullName;

    @Setter
    @Getter
    @NotBlank
    @Column(unique = true)
    private String userName;

    @Setter
    @Getter
    @Email
    @NotBlank
    @Column(unique = true)
    private String userEmail;

    @Setter
    @Getter
    @NotBlank
    private String userLocation;

    @Setter
    @Getter
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Setter
    @Getter
    @NotBlank
    private String userAuthProvider;

    @Setter
    @Getter
    @NotBlank
    private String userPassword;

    @Setter
    @Getter
    @OneToMany(mappedBy = "userCatOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cat> userCats = new ArrayList<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "userEventPlanner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> userPlannedEvents = new ArrayList<>();

    @Setter
    @Getter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EventParticipant> userEventParticipants = new ArrayList<>();

    @Setter
    @Getter
    @OneToMany(mappedBy = "userPostAuthor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> userPost = new ArrayList<>();


    public Long getUserId() {
        return userId;
    }

    public @NotBlank String getUserFullName() {
        return userFullName;
    }
}
