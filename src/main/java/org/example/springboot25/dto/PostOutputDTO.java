package org.example.springboot25.dto;

import java.time.LocalDateTime;

public class PostOutputDTO {

    private Long id;
    private String postText;
    private String postImageUrl;
    private LocalDateTime postCreatedAt;
    private Long userPostAuthorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUserPostAuthorId() {
        return userPostAuthorId;
    }

    public void setUserPostAuthorId(Long userPostAuthorId) {
        this.userPostAuthorId = userPostAuthorId;
    }
}
