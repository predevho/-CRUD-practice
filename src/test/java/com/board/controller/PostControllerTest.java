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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void t1_게시글_생성() throws Exception {
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "title": "테스트 제목",
                                          "content": "테스트 내용"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.content").value("테스트 내용"));
    }

    @Test
    void t2_제목이_비어있으면_400을_반환() throws Exception {
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "title": "",
                                        "content": "테스트 내용"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("제목은 필수 입니다."));

    }

    @Test
    void t3_게시글_제목을_검색한다() throws Exception {
        postRepository.save(
                new Post("Spring Boot 학습", "검색 테스트")
        );

        mockMvc.perform(
                        get("/posts")
                                .param("keyword", "Spring")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].title")
                        .value(hasItem("Spring Boot 학습")));
    }

    @Test
    void t3_게시글_목록에_댓글이_포함된다() throws Exception {
        Post post = postRepository.save(new Post("댓글 포함 게시글", "댓글 테스트 내용"));

        commentRepository.save(new Comment("테스트 내용", post));

        mockMvc.perform(
                        get("/posts")
                                .param("size", "100")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].title")  
                    .value(hasItem("댓글 포함 게시글")))
                .andExpect(jsonPath("$.content[*].comments[*].content")
                    .value(hasItem("테스트 내용")));
    }
}
