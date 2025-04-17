package org.example.springboot25.service;

import org.example.springboot25.dto.CommentInputDTO;
import org.example.springboot25.dto.CommentOutputDTO;
import org.example.springboot25.entities.Comment;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CommentMapper;
import org.example.springboot25.repository.CommentRepository;
import org.example.springboot25.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }

    public CommentOutputDTO createComment(CommentInputDTO dto, User author) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found"));
        Comment comment = commentMapper.toComment(dto, author, post);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public List<CommentOutputDTO> getAllCommentsForPost(Long postId) {
        return commentRepository.findAllByPost_PostId(postId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getAuthor().getUserId().equals(currentUser.getUserId()) && currentUser.getUserRole().name() != "ADMIN") {
            throw new SecurityException("You do not have permission to delete this comment.");
        }
        commentRepository.delete(comment);
    }
}