package com.board.controller;

import com.board.domain.Post;
import com.board.dto.PostCreateRequest;
import com.board.dto.PostResponse;
import com.board.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public PostResponse createPost(@RequestBody PostCreateRequest request) {
        return postService.createPost(
                request.getTitle(),
                request.getContent()
        );
    }
}
