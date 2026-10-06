package com.board.post;

import com.board.dto.PostCreateRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostCreateRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Validator 초기화
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void t1_제목이_비어있으면_검증에_실패한다() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle(""); // 제목이 비어있음
        request.setContent("정상적인 내용");

        var violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
