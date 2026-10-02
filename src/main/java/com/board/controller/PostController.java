package com.board.controller;

import com.board.domain.Post;
import com.board.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public Post createPost(@RequestParam String title,
                           @RequestParam String content) {
        return postService.createPost(title, content);
    }
}
