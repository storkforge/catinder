package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Post;
import org.example.springboot25.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts") // API-endpoints för inlägg
public class PostController {

    private final PostService postService;

    // Konstruktor-injektion av PostService
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Hämta alla inlägg
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Hämta ett specifikt inlägg
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    // Skapa ett nytt inlägg
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody @Valid Post post) {
        return postService.createPost(post);
    }

    // Ta bort ett inlägg
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    // Uppdatera ett inlägg
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post updatePost(@PathVariable Long id, @RequestBody @Valid Post post) {
        return postService.updatePost(id, post);
    }

    // Uppdatera delar av ett inlägg
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post patchPost(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return postService.patchPost(id, updates);
    }
}
