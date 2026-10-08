package com.board.dto;

import com.board.domain.Comment;

public class CommentResponse {

    private final Long id;

    private final String content;

    private final Long postId;

    public CommentResponse(Long id, String content, Long postId) {
        this.id = id;
        this.content = content;
        this.postId = postId;
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getPost().getId()
        );
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getPostId() {
        return postId;
    }

}
