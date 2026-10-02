package com.board.service;

import com.board.domain.Post;
import com.board.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final PostRepository postRepository;

    public HelloService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public String createGreeting(String name) {
        return "Hello, " + name;
    }

}
