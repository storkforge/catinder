package org.example.springboot25.controller;

import org.example.springboot25.dto.PostInputDTO;
import org.example.springboot25.dto.PostOutputDTO;
import org.example.springboot25.dto.PostUpdateDTO;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.PostMapper;
import org.example.springboot25.service.PostService;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostGraphQLController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    public PostGraphQLController(PostService postService, PostMapper postMapper, UserService userService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.userService = userService;
    }

    @QueryMapping
    public List<PostOutputDTO> getAllPosts() {
        return postService.getAllPosts().stream().map(postMapper::toDTO).collect(Collectors.toList());
    }

    @QueryMapping
    public PostOutputDTO getPostById(@Argument Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }

        Post post = postService.getPostById(id);
        if (post == null) {
            throw new NotFoundException("Post with ID: " + id + " not found");
        }

        return postMapper.toDTO(post);
    }


    @MutationMapping
    public PostOutputDTO createPost(@Argument("input") PostInputDTO input) {
        if (input == null || input.getUserPostAuthorId() == null) {
            throw new IllegalArgumentException("Invalid post input");
        }
        User user = userService.getUserById(input.getUserPostAuthorId());
        if (user == null) {
            throw new NotFoundException("User with ID: " + input.getUserPostAuthorId() + " not found");
        }
        Post post = postService.createPost(postMapper.toEntityInput(input, user));
        return postMapper.toDTO(post);
    }

    @MutationMapping
    public PostOutputDTO updatePost(@Argument Long id, @Argument("input") PostUpdateDTO input) {
        if (id == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
        if (input == null) {
            throw new IllegalArgumentException("Update input cannot be null");
        }
        Post existingPost = postService.getPostById(id);
        if (existingPost == null) {
            throw new NotFoundException("Post with ID: " + id + " not found");
        }
        Post updatedPost = postMapper.updateFromDto(input, existingPost);
        Post savedPost = postService.updatePost(id, updatedPost);
        return postMapper.toDTO(savedPost);
    }

    @MutationMapping
    public boolean deletePost(@Argument Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }

        Post post = postService.getPostById(id);
        if (post == null) {
            throw new NotFoundException("Post with ID: " + id + " not found");
        }
        postService.deletePost(id);
        return true;
    }
}

