package com.board.service;

import com.board.domain.Post;
import com.board.dto.PostResponse;
import com.board.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse createPost(String title, String content) {
        Post post = new Post(title, content);
        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
    }
}
