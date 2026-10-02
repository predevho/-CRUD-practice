package com.board.service;

import com.board.domain.Post;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String createGreeting(String name) {
        return "Hello, " + name;
    }

    public Post createSamplePost() {
        return new Post(
                1L,
                "첫 번째 게시글",
                "Spring Boot 학습을 시작했습니다."
        );
    }
}
