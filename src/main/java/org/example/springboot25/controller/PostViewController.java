package org.example.springboot25.controller;

import org.example.springboot25.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "post/post";
    }
}
