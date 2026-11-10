package com.cinemanetwork.domain.review.service;

import com.cinemanetwork.domain.movie.entity.Movie;
import com.cinemanetwork.domain.movie.service.MovieService;
import com.cinemanetwork.domain.review.dto.CreateReviewRequest;
import com.cinemanetwork.domain.review.dto.ReviewResponse;
import com.cinemanetwork.domain.review.entity.Review;
import com.cinemanetwork.domain.review.entity.ReviewLike;
import com.cinemanetwork.domain.review.repository.ReviewLikeRepository;
import com.cinemanetwork.domain.review.repository.ReviewRepository;
import com.cinemanetwork.domain.user.entity.Role;
import com.cinemanetwork.domain.user.entity.User;
import com.cinemanetwork.domain.user.service.UserService;
import com.cinemanetwork.global.exception.CustomException;
import com.cinemanetwork.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserService userService;
    private final MovieService movieService;

    @Transactional
    public ReviewResponse createReview(Long userId, Long movieId, CreateReviewRequest request) {
        User user = userService.getUserEntity(userId);
        Movie movie = movieService.getMovieEntity(movieId);

        if (reviewRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new CustomException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = Review.builder()
                .user(user)
                .movie(movie)
                .rating(request.getRating())
                .content(request.getContent())
                .build();

        Review savedReview = reviewRepository.save(review);
        updateMovieRatingStats(movie);

        return ReviewResponse.of(savedReview, false);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getMovieReviews(Long movieId, Long currentUserId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByMovieId(movieId, pageable);
        return reviews.map(r -> {
            boolean isLiked = currentUserId != null && reviewLikeRepository.existsByUserIdAndReviewId(currentUserId, r.getId());
            return ReviewResponse.of(r, isLiked);
        });
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getMyReviews(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(r -> ReviewResponse.of(r, false))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        User user = userService.getUserEntity(userId);
        if (!review.getUser().getId().equals(userId) && user.getRole() != Role.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        Movie movie = review.getMovie();
        reviewRepository.delete(review);
        updateMovieRatingStats(movie);
    }

    @Transactional
    public boolean toggleReviewLike(Long userId, Long reviewId) {
        User user = userService.getUserEntity(userId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Optional<ReviewLike> existingLike = reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId);
        if (existingLike.isPresent()) {
            reviewLikeRepository.delete(existingLike.get());
            review.decrementLikes();
            return false;
        } else {
            ReviewLike like = ReviewLike.builder()
                    .user(user)
                    .review(review)
                    .build();
            reviewLikeRepository.save(like);
            review.incrementLikes();
            return true;
        }
    }

    private void updateMovieRatingStats(Movie movie) {
        Double avg = reviewRepository.getAverageRatingByMovieId(movie.getId());
        int count = reviewRepository.countByMovieId(movie.getId());

        BigDecimal ratingAvg = avg != null ? BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        movie.updateRatingStats(ratingAvg, count);
    }
}
