package com.cinemanetwork.domain.review.dto;

import com.cinemanetwork.domain.review.entity.Review;
import com.cinemanetwork.domain.user.dto.UserProfileResponse;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private BigDecimal rating;
    private String content;
    private Integer likesCount;
    private Boolean isLiked;
    private UserProfileResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse of(Review review, boolean isLiked) {
        return ReviewResponse.builder()
                .id(review.getId())
                .movieId(review.getMovie().getId())
                .movieTitle(review.getMovie().getTitle())
                .rating(review.getRating())
                .content(review.getContent())
                .likesCount(review.getLikesCount())
                .isLiked(isLiked)
                .user(UserProfileResponse.from(review.getUser()))
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
