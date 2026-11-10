package com.cinemanetwork.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(min = 1, max = 1000, message = "댓글은 1000자 이내로 입력해주세요.")
    private String content;
}
