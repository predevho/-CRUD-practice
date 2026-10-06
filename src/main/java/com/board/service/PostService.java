package com.board.service;

import com.board.domain.Post;
import com.board.dto.PostResponse;
import com.board.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    public List<PostResponse> findAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "게시글을 찾을 수 없습니다."
                        )
                );

        return PostResponse.from(post);
    }

    public PostResponse updatePost(Long id, String title, String content) {
        Post post = postRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "게시글을 찾을 수 없습니다."
                        )
                );
        post.update(title, content);
        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
    }
}
