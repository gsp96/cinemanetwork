package com.cinemanetwork.domain.post.dto;

import com.cinemanetwork.domain.post.entity.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostRequest {

    @NotNull(message = "카테고리를 선택해주세요.")
    private PostCategory category;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 2, max = 200, message = "제목은 2자 이상 200자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private Boolean isSecret;
}
