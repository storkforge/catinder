package org.example.springboot25.mapper;

import org.example.springboot25.dto.PostInputDTO;
import org.example.springboot25.dto.PostOutputDTO;
import org.example.springboot25.dto.PostUpdateDTO;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class PostMapper {

    public Post toEntityInput(PostInputDTO input, User user) {
        if (input == null) {
            throw new IllegalArgumentException("PostInputDTO cannot be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        Post post = new Post();
        post.setPostText(input.getPostText());
        post.setPostImageUrl(input.getPostImageUrl());
        post.setUser(user);
        post.setPostCreatedAt(LocalDateTime.now());
        return post;
    }

    public PostOutputDTO toDTO(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }
        PostOutputDTO dto = new PostOutputDTO();
        dto.setId(post.getPostId());
        dto.setPostText(post.getPostText());
        dto.setPostImageUrl(post.getPostImageUrl());
        if (post.getPostCreatedAt() != null) {
            dto.setPostCreatedAt(post.getPostCreatedAt().atOffset(ZoneOffset.UTC));
        }
        dto.setUserPostAuthorId(post.getUser().getUserId());
        return dto;

    }

    public Post updateFromDto(PostUpdateDTO dto, Post post) {
        if (dto == null) {
            throw new IllegalArgumentException("PostUpdateDTO cannot be null");
        }
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }
        if (dto.getPostText() != null) {
            post.setPostText(dto.getPostText());
        }
        if (dto.getPostImageUrl() != null) {
            post.setPostImageUrl(dto.getPostImageUrl());
        }
        return post;
    }
}



