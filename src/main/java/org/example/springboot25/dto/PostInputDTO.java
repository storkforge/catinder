package org.example.springboot25.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "PostInputDTO{" +
                "postText='" + postText + '\'' +
                ", postImageUrl='" + postImageUrl + '\'' +
                ", userPostAuthorId=" + userPostAuthorId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostInputDTO that = (PostInputDTO) o;
        return Objects.equals(postText, that.postText) &&
                Objects.equals(postImageUrl, that.postImageUrl) &&
                Objects.equals(userPostAuthorId, that.userPostAuthorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postText, postImageUrl, userPostAuthorId);
    }


}
