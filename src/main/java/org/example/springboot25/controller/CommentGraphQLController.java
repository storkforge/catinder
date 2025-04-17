package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.CommentInputDTO;
import org.example.springboot25.dto.CommentOutputDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.CommentService;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CommentGraphQLController {

    private final CommentService commentService;
    private final UserService userService;

    public CommentGraphQLController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @QueryMapping
    public List<CommentOutputDTO> getCommentsByPost(@Argument Long postId) {
        return commentService.getAllCommentsForPost(postId);
    }

    @MutationMapping
    public CommentOutputDTO createComment(@Argument @Valid CommentInputDTO input, Authentication auth) {
        if (auth == null) {
            throw new IllegalArgumentException("Authentication is required");
        }
        User currentUser = userService.findUserByUserName(auth.getName());
        return commentService.createComment(input, currentUser);
    }

    @MutationMapping
    public boolean deleteComment(@Argument Long commentId, Authentication auth) {
        if (auth == null) {
            throw new IllegalArgumentException("Authentication is required");
        }
        try {
            User currentUser = userService.findUserByUserName(auth.getName());
            commentService.deleteComment(commentId, currentUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
