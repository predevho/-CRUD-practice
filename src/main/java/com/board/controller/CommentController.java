package com.board.controller;

import com.board.dto.CommentCreateRequest;
import com.board.dto.CommentResponse;
import com.board.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public CommentResponse createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request) {

        return commentService.createComment(postId, request);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> findComments(@PathVariable Long postId) {
        return commentService.findComments(postId);
    }
}
