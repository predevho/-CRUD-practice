package com.board.controller;

import com.board.domain.Post;
import com.board.dto.PostCreateRequest;
import com.board.dto.PostResponse;
import com.board.dto.PostUpdateRequest;
import com.board.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/posts")
    public List<PostResponse> findAllPosts() {
        return postService.findAllPosts();
    }

    @GetMapping("/posts/{id}")
    public PostResponse findPost(@PathVariable Long id) {
        return postService.findPost(id);
    }

    @PutMapping("/posts/{id}")
    public PostResponse updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest request
    ) {
        return postService.updatePost(
                id,
                request.getTitle(),
                request.getContent()
        );
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
