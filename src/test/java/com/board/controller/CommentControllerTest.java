package com.board.controller;

import com.board.domain.Comment;
import com.board.domain.Post;
import com.board.repository.CommentRepository;
import com.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private CommentRepository commentRepository;

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

    @Test
    void t3_게시글의_댓글을_조회한다() throws Exception {
        Post post = postRepository.save(new Post("제목", "내용"));
        commentRepository.save(new Comment("조회 테스트 댓글", post));

        mockMvc.perform(
                        get("/posts/" + post.getId() + "/comments")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].content")
                        .value(hasItem("조회 테스트 댓글")))
                .andExpect(jsonPath("$[*].postId")
                        .value(hasItem(post.getId().intValue())));
    }

}
