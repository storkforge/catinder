package org.example.springboot25.service;

import org.example.springboot25.entities.Post;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));
        postRepository.delete(post);
    }

    public Post updatePost(Long id, Post updatedPost) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        existing.setPostText(updatedPost.getPostText());
        existing.setPostImageUrl(updatedPost.getPostImageUrl());
        existing.setPostCreatedAt(updatedPost.getPostCreatedAt());

        return postRepository.save(existing);
    }

    public Post patchPost(Long id, Map<String, Object> updates) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        if (updates.get("postText") instanceof String text) {
            existing.setPostText(text);
        }

        if (updates.get("postImageUrl") instanceof String imageUrl) {
            existing.setPostImageUrl(imageUrl);
        }

        if (updates.get("postCreatedAt") instanceof String dateTimeStr) {
            try {
                existing.setPostCreatedAt(LocalDateTime.parse(dateTimeStr));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid format for 'postCreatedAt'. Use ISO 8601 format.");
            }
        }

        return postRepository.save(existing);
    }
}
