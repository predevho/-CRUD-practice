package com.board.controller;

import com.board.domain.Post;
import com.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    void t1_댓글을_등록한다() throws Exception {
        Post post = postRepository.save(new Post("제목", "내용"));

        mockMvc.perform(
                        post("/posts/" + post.getId() + "/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "content": "테스트 댓글"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("테스트 댓글"))
                .andExpect(jsonPath("$.postId").value(post.getId()));
    }

    @Test
    void t2_댓글이_비어있으면_400을_반환한다() throws Exception {
        Post post = postRepository.save(new Post("제목", "내용"));

        mockMvc.perform(
                post("/posts/" + post.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "    "
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content")
                .value("댓글 내용은 필수 입니다."));
    }

}
