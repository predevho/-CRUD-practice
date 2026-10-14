package com.board.dto;

import com.board.domain.Post;

import java.util.List;

public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    private List<CommentResponse> comments;

    public PostResponse(
            Long id,
            String title,
            String content,
            List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }

    public static PostResponse from(Post post) {
        List<CommentResponse> comments = post.getComments()
                .stream()
                .map(CommentResponse::from)
                .toList();

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                comments
        );
    }

    public static PostResponse from(
            Post post,
            List<CommentResponse> comments
    ) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                comments
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }
}
