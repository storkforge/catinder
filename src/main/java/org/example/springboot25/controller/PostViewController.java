package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.PostService;
import org.example.springboot25.service.UserService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/posts")
public class PostViewController {

    private final PostService postService;
    private final UserService userService;

    public PostViewController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String showAllPosts(Principal principal, Model model) {
        User currentUser = null;
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email");
            currentUser = userService.findUserByEmail(email);
        } else if (principal != null) {
            currentUser = userService.findUserByUserName(principal.getName());
        }
        model.addAttribute("posts", postService.getAllPostsOrderByDate());
        model.addAttribute("currentUser", currentUser);
        return "post/post-list";
    }

    @GetMapping("/add")
    String addPostForm(Model model) {
        if (!model.containsAttribute("post")) {
            model.addAttribute("post", new Post());
        }
        return "post/post-add";
    }

    @PostMapping("/add")
    public String processCreateNewPostForm(@ModelAttribute Post post, Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email");
            User user = userService.findUserByEmail(email);
            post.setUserPostAuthor(user);
            post.setPostCreatedAt(LocalDateTime.now());
            postService.createPost(post);
        } else {
            throw new IllegalStateException("Unexpected authentication type: " + principal.getClass().getName());
        }
        return "redirect:/posts";
    }

    @GetMapping("/{postId}/edit")
    public String showUpdateForm(@PathVariable Long postId, Model model) {
        try {
            Post post = postService.getPostById(postId);
            model.addAttribute("post", post);
            return "post/post-update";
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }

    @PutMapping("/{postId}")
    public String updatePost(@PathVariable Long postId, @ModelAttribute Post post, RedirectAttributes redirectAttributes) {
        try {
            postService.updatePost(postId, post);
            redirectAttributes.addFlashAttribute("update_success", "Post Updated!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/posts/" + postId + "/edit";
        }
        return "redirect:/posts/" + postId + "/edit";
    }

    @GetMapping("/{postId}/delete")
    public String getDeleteForm(@PathVariable Long postId, Model model) {
        try {
            Post post = postService.getPostById(postId);
            model.addAttribute("post", post);
            return "post/post-update";
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }

    @DeleteMapping("/{postId}/delete")
    String deletePost(@PathVariable Long postId, RedirectAttributes redirectAttributes, Model model) {
        try {
            postService.deletePost(postId);
            redirectAttributes.addFlashAttribute("delete_success", "Post deleted!");
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
        return "redirect:/posts";
    }
}
