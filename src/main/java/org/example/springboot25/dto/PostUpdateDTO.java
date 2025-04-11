package org.example.springboot25.dto;

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
}
