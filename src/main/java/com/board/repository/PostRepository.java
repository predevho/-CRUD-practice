package com.board.repository;

import com.board.domain.Post;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {

    public Post findSamplePost() {
        return new Post(
                1L,
                "첫 번째 게시글",
                "Spring Boot 학습을 시작했습니다."
        );
    }
}
