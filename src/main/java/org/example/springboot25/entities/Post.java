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

    public @NotBlank String getPostText() {
        return postText;
    }

    public void setPostText(@NotBlank String postText) {
        this.postText = postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public @PastOrPresent LocalDateTime getPostCreatedAt() {
        return postCreatedAt;
    }

    public void setPostCreatedAt(@PastOrPresent LocalDateTime postCreatedAt) {
        this.postCreatedAt = postCreatedAt;
    }

    public @NotNull User getUserPostAuthor() {
        return userPostAuthor;
    }

    public void setUserPostAuthor(@NotNull User userPostAuthor) {
        this.userPostAuthor = userPostAuthor;
    }

}
