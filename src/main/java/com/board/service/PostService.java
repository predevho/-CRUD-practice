package com.board.service;

import com.board.domain.Post;
import com.board.dto.CommentResponse;
import com.board.dto.PostResponse;
import com.board.repository.CommentRepository;
import com.board.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public PostResponse createPost(String title, String content) {
        Post post = new Post(title, content);
        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponse findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "게시글을 찾을 수 없습니다."
                        )
                );

        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse updatePost(Long id, String title, String content) {
        Post post = postRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "게시글을 찾을 수 없습니다."
                        )
                );
        post.update(title, content);

        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> searchPosts(String keyword) {
        return postRepository.findByTitleContaining(keyword)
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> findPosts(String keyword, Pageable pageable) {
        Page<Post> posts;

        if (keyword == null || keyword.isBlank()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTitleContaining(keyword, pageable);
        }

        Map<Long, List<CommentResponse>> commentsByPostId;

        if (posts.isEmpty()) {
            commentsByPostId = Map.of();
        } else {
            List<Long> postIds = posts.getContent()
                    .stream()
                    .map(Post::getId)
                    .toList();

            commentsByPostId = commentRepository.findByPost_IdIn(postIds)
                    .stream()
                    .map(CommentResponse::from)
                    .collect(Collectors.groupingBy(
                            CommentResponse::getPostId
                    ));
        }

        return posts.map(post ->
                PostResponse.from(
                        post,
                        commentsByPostId.getOrDefault(
                                post.getId(),
                                List.of()
                        )
                )
        );
    }

}
