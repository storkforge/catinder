package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.exceptions.UserAlreadyExistsException;
import org.example.springboot25.repository.UserRepository;
import org.example.springboot25.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/posts")
public class PostViewController {

    private final PostService postService;

    //Todo: Remove when security is implemented
    @Autowired
    private UserRepository userRepository;

    public PostViewController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String showAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
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
    String addPostForm(@Valid @ModelAttribute Post post, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/post-add";
        }
        try {
            // Todo: Remove temp user when security is implemented
            User dummyUser = userRepository.findByUserName("dummyUser")
                    .orElseGet(() -> {
                        User newDummy = new User();
                        newDummy.setUserFullName("Dummy User");
                        newDummy.setUserName("dummyUser");
                        newDummy.setUserEmail("dummy@example.com");
                        newDummy.setUserLocation("DummyVille");
                        newDummy.setUserRole(UserRole.BASIC);
                        newDummy.setUserAuthProvider("testProvider");
                        newDummy.setUserPassword("dummyPassword");
                        return userRepository.save(newDummy);
                    });
            //Todo: Add when security is implemented
//            CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
//            User currentUser = userDetails.getUser();

            //Todo: Change param to currentUser when security is implemented
            post.setUserPostAuthor(dummyUser);
            post.setPostCreatedAt(LocalDateTime.now());
            postService.createPost(post);
            redirectAttributes.addFlashAttribute("success", "Account created!");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "post/post-add";
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
            return "error-page";
        }
    }

    @PutMapping("/{postId}")
    public String updatePost(@PathVariable Long postId, @Valid @ModelAttribute Post post, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/post-update";
        }
        try {
            postService.updatePost(postId, post);
            redirectAttributes.addFlashAttribute("update_success", "Post Updated!");
        } catch (Error ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/posts/" + postId + "/edit";
        }
        return "redirect:/posts/" + postId + "/edit";
    }

    @DeleteMapping("/{postId}")
    String deletePost(@PathVariable Long postId, RedirectAttributes redirectAttributes, Model model) {
        try {
            postService.deletePost(postId);
            redirectAttributes.addFlashAttribute("delete_success", "Post deleted!");
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error-page";
        }
        return "redirect:/posts";
    }
}
