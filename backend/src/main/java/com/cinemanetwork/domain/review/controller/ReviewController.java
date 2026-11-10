package com.cinemanetwork.domain.review.controller;

import com.cinemanetwork.domain.review.dto.CreateReviewRequest;
import com.cinemanetwork.domain.review.dto.ReviewResponse;
import com.cinemanetwork.domain.review.service.ReviewService;
import com.cinemanetwork.global.dto.ApiResponse;
import com.cinemanetwork.global.security.service.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "04. Review", description = "영화 평점 및 리뷰 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "영화별 리뷰 목록 조회")
    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getMovieReviews(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long currentUserId = userPrincipal != null ? userPrincipal.getId() : null;
        Page<ReviewResponse> response = reviewService.getMovieReviews(movieId, currentUserId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "영화 리뷰 및 평점 작성")
    @PostMapping("/movies/{movieId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(userPrincipal.getId(), movieId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("리뷰가 등록되었습니다.", response));
    }

    @Operation(summary = "내 리뷰 목록 조회")
    @GetMapping("/users/me/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getMyReviews(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ReviewResponse> response = reviewService.getMyReviews(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewService.deleteReview(userPrincipal.getId(), reviewId);
        return ResponseEntity.ok(ApiResponse.success("리뷰가 삭제되었습니다.", null));
    }

    @Operation(summary = "리뷰 좋아요 토글")
    @PostMapping("/reviews/{reviewId}/like")
    public ResponseEntity<ApiResponse<Boolean>> toggleLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isLiked = reviewService.toggleReviewLike(userPrincipal.getId(), reviewId);
        String msg = isLiked ? "리뷰를 추천했습니다." : "추천을 취소했습니다.";
        return ResponseEntity.ok(ApiResponse.success(msg, isLiked));
    }
}
