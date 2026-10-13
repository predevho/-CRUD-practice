package com.board.service;

import com.board.domain.Comment;
import com.board.domain.Post;
import com.board.dto.CommentCreateRequest;
import com.board.dto.CommentResponse;
import com.board.repository.CommentRepository;
import com.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void t1_댓글을_등록한다() {
        Long postId = 1L;
        Post post = new Post("제목", "내용");
        Comment comment = new Comment("댓글 내용", post);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentCreateRequest request = new CommentCreateRequest("댓글 내용");
        CommentResponse response = commentService.createComment(postId, request);

        assertThat(response.getContent()).isEqualTo("댓글 내용");
    }

    @Test
    void t2_존재하지_않는_게시글에는_댓글을_등록할_수_없다() {
        Long postId = 999L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        CommentCreateRequest request = new CommentCreateRequest("댓글 내용");

        assertThatThrownBy(() -> commentService.createComment(postId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }

    @Test
    void t3_게시글의_댓글을_조회한다() {
        Long postId = 1L;

        Post post = new Post("제목", "내용");
        Comment comment = new Comment("댓글 내용", post);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findAllByPost_id(postId)).thenReturn(List.of(comment));

        List<CommentResponse> responses = commentService.findComments(postId);

        assertThat(responses).hasSize(1);

        assertThat(responses.get(0).getContent())
                .isEqualTo("댓글 내용");
    }
}
