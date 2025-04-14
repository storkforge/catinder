package org.example.springboot25.dto;

import java.util.Objects;

public class PostUpdateDTO {

    private String postText;
    private String postImageUrl;


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

    @Override
    public String toString() {
        return "PostUpdateDTO{" +
                "postText='" + postText + '\'' +
                ", postImageUrl='" + postImageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostUpdateDTO that = (PostUpdateDTO) o;
        return Objects.equals(postText, that.postText) &&
                Objects.equals(postImageUrl, that.postImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postText, postImageUrl);
    }

}
