package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotBlank
    private String postText;

    private String postImageUrl;

    @PastOrPresent
    private LocalDateTime postCreatedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_author_user_id")
    private User userPostAuthor;

    public Long getPostId() {
        return postId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public LocalDateTime getPostCreatedAt() {
        return postCreatedAt;
    }

    public void setPostCreatedAt(LocalDateTime postCreatedAt) {
        this.postCreatedAt = postCreatedAt;
    }

    public User getUser() {
        return userPostAuthor;
    }

    public void setUser(User user) {
        this.userPostAuthor = user;
    }

    public void setUserPostAuthor(User user) {
        this.userPostAuthor = user;
    }
}
