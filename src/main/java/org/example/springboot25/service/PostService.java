package org.example.springboot25.service;

import org.example.springboot25.entities.Post;
import org.example.springboot25.exceptions.PostNotFoundException;
import org.example.springboot25.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    private final PostRepository postRepository;

    // Konstruktor för dependency injection
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Hämta alla inlägg
    @Transactional
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Hämta ett specifikt inlägg
    @Transactional
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Inlägg med id " + id + " hittades inte"));
    }

    // Skapa ett nytt inlägg
    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // Ta bort ett inlägg
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Inlägg med id " + id + " hittades inte"));
        postRepository.delete(post);
    }

    // Uppdatera ett inlägg
    @Transactional
    public Post updatePost(Long id, Post updatedPost) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post med id " + id + " hittades inte"));

        existing.setPostText(updatedPost.getPostText());
        existing.setPostImageUrl(updatedPost.getPostImageUrl());
        existing.setPostCreatedAt(updatedPost.getPostCreatedAt());

        return postRepository.save(existing);
    }

    // Uppdatera delar av ett inlägg
    @Transactional
    public Post patchPost(Long id, Map<String, Object> updates) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post med id " + id + " hittades inte"));

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
                throw new IllegalArgumentException("Ogiltigt datumformat för 'postCreatedAt'. Använd ISO 8601.");
            }
        }

        return postRepository.save(existing);
    }
}
