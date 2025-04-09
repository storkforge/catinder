package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.PostService;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostRESTController {

    private final PostService postService;
    private final UserService userService;

    public PostRESTController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    /**
     * Helper method to check if current user is owner or admin
     */

    private boolean isNotOwnerOrAdmin(Post post, User currentUser) {
        boolean isOwner = post.getUser().getUserName().equals(currentUser.getUserName());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    // Authenticated users can view all posts
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Authenticated users can view a specific post by ID
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    // BASIC and PREMIUM users can create a post
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody @Valid Post post, Authentication auth) {
        String username = auth.getName();
        User currentUser = userService.getUserByUserName(username);

        if (currentUser == null) {
            throw new NotFoundException("Authenticated user not found: " + username);
        }

        post.setUser(currentUser);
        post.setPostCreatedAt(LocalDateTime.now());
        return postService.createPost(post);
    }

    // Only post owner or ADMIN can delete
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id, Authentication auth) {
        Post post = postService.getPostById(id);
        User currentUser = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(post, currentUser)) {
            throw new AccessDeniedException("You can only delete your own post");
        }
        postService.deletePost(id);
    }

    // Only post owner or ADMIN can fully update (PUT)
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post updatePost(@PathVariable Long id,@RequestBody @Valid Post updatedPost, Authentication auth) {
        Post post = postService.getPostById(id);
        User currentUser = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(post, currentUser)) {
            throw new AccessDeniedException("You can only update your own post");
        }
        return postService.updatePost(id, updatedPost);
    }

    // Only post owner or ADMIN can partially update (PATCH)
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post patchPost(@PathVariable Long id, @RequestBody Map<String, Object> updates, Authentication auth) {
        Post existing = postService.getPostById(id);
        User currentUser = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(existing, currentUser)) {
            throw new AccessDeniedException("You can only update your own post");
        }
        return postService.patchPost(id, updates);
    }
}
