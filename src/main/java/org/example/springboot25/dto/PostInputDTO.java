package org.example.springboot25.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostInputDTO {

    @NotBlank
    private String postText;

    @NotBlank
    private String postImageUrl;

    @NotNull
    private Long userPostAuthorId;

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

    public Long getUserPostAuthorId() {
        return userPostAuthorId;
    }

    public void setUserPostAuthorId(Long userPostAuthorId) {
        this.userPostAuthorId = userPostAuthorId;
    }
}
