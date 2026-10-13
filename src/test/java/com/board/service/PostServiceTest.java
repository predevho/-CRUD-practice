package com.board.service;

import com.board.domain.Post;
import com.board.dto.PostResponse;
import com.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void t1_게시글을_삭제한다() {
        Long postId = 1L;

        postService.deletePost(postId);

        verify(postRepository).deleteById(postId);
    }

    @Test
    void t2_제목으로_게시글을_검색한다() {
        Post post = new Post("Spring Boot 학습", "검색 테스트");

        when(postRepository.findByTitleContaining("Spring")).thenReturn(List.of(post));
        List<PostResponse> responses = postService.searchPosts("Spring");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTitle()).isEqualTo("Spring Boot 학습");
    }
}
