package com.board.controller;

import com.board.dto.PostCreateRequest;
import com.board.dto.PostResponse;
import com.board.dto.PostUpdateRequest;
import com.board.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public PostResponse createPost(
            @Valid @RequestBody PostCreateRequest request
    ) {
        return postService.createPost(
                request.getTitle(),
                request.getContent()
        );
    }

    @GetMapping("/posts/{id}")
    public PostResponse findPost(@PathVariable Long id) {
        return postService.findPost(id);
    }

    @PutMapping("/posts/{id}")
    public PostResponse updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request
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

    @GetMapping("/posts")
    public Page<PostResponse> findPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        return postService.findPosts(keyword, pageable);
    }
}
