package org.example.springboot25.mapper;

import org.example.springboot25.dto.CommentInputDTO;
import org.example.springboot25.dto.CommentOutputDTO;
import org.example.springboot25.entities.Comment;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public Comment toComment(CommentInputDTO dto, User author, Post post) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }

    public CommentOutputDTO toDto(Comment comment) {
        CommentOutputDTO dto = new CommentOutputDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setText(comment.getText());
        dto.setAuthorId(comment.getAuthor().getUserId());
        dto.setAuthorName(comment.getAuthor().getUserName());
        dto.setPostId(comment.getPost().getPostId());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}