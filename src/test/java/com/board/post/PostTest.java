package com.board.post;

import com.board.domain.Post;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    void t1_게시글_내용을_수정한다() {
        Post post = new Post(
                "수정 전 제목",
                "수정 전 내용"
        );

        post.update(
                "수정 후 제목",
                "수정 후 내용"
        );

        assertThat(post.getTitle()).isEqualTo("수정 후 제목");
        assertThat(post.getContent()).isEqualTo("수정 후 내용");
    }
}