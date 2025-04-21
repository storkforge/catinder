package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.CommentInputDTO;
import org.example.springboot25.dto.CommentOutputDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.CommentService;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    private final UserService userService;

    public CommentRestController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    /**
     * Skapar en ny kommentar kopplad till ett inlägg
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CommentOutputDTO> create(@RequestBody @Valid CommentInputDTO input, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        CommentOutputDTO created = commentService.createComment(input, currentUser);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Hämtar alla kommentarer för ett specifikt inlägg
     */
    @GetMapping("/post/{postId}")
    public List<CommentOutputDTO> getComments(@PathVariable Long postId) {
        return commentService.getAllCommentsForPost(postId);
    }

    /**
     * Raderar en kommentar – endast skaparen eller admin kan göra det
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
