package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.UserAlreadyExistsException;
import org.example.springboot25.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostViewController {

    private final PostService postService;

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
            postService.createPost(post);
            redirectAttributes.addFlashAttribute("success", "Account created!");
        } catch (Exception ex) {
            model.addAttribute("error", "Something went wrong!");
            return "post/post-add";
        }
        return "redirect:/posts";
    }
}
