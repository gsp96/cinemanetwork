package com.cinemanetwork.domain.review.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateReviewRequest {

    @NotNull(message = "평점을 선택해주세요.")
    @DecimalMin(value = "0.5", message = "최소 평점은 0.5점입니다.")
    @DecimalMax(value = "5.0", message = "최대 평점은 5.0점입니다.")
    private BigDecimal rating;

    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    @Size(min = 2, max = 1000, message = "리뷰는 2자 이상 1000자 이하로 작성해주세요.")
    private String content;
}
