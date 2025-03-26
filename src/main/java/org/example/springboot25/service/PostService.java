package org.example.springboot25.service;

import org.example.springboot25.entities.Post;
import org.example.springboot25.exceptions.PostNotFoundException;
import org.example.springboot25.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
