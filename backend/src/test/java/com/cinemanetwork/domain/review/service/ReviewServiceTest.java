package com.cinemanetwork.domain.review.service;

import com.cinemanetwork.domain.movie.entity.Movie;
import com.cinemanetwork.domain.movie.repository.MovieRepository;
import com.cinemanetwork.domain.review.dto.CreateReviewRequest;
import com.cinemanetwork.domain.review.dto.ReviewResponse;
import com.cinemanetwork.domain.user.entity.Role;
import com.cinemanetwork.domain.user.entity.User;
import com.cinemanetwork.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("리뷰 작성 시 영화 평균 평점과 리뷰 수가 자동 갱신된다")
    void testCreateReviewAndRecalculateRating() {
        // given
        User user1 = userRepository.save(User.builder()
                .email("reviewer1@test.com")
                .password("pw1234")
                .nickname("평론가1")
                .role(Role.USER)
                .build());

        User user2 = userRepository.save(User.builder()
                .email("reviewer2@test.com")
                .password("pw1234")
                .nickname("평론가2")
                .role(Role.USER)
                .build());

        Movie movie = movieRepository.save(Movie.builder()
                .title("테스트 영화")
                .genres("드라마")
                .build());

        // when 1
        CreateReviewRequest req1 = new CreateReviewRequest();
        ReflectionTestUtils.setField(req1, "rating", new BigDecimal("5.0"));
        ReflectionTestUtils.setField(req1, "content", "인생 최고의 명작입니다!");
        ReviewResponse res1 = reviewService.createReview(user1.getId(), movie.getId(), req1);

        // then 1
        Movie updatedMovie1 = movieRepository.findById(movie.getId()).orElseThrow();
        assertThat(updatedMovie1.getReviewCount()).isEqualTo(1);
        assertThat(updatedMovie1.getRatingAvg()).isEqualByComparingTo(new BigDecimal("5.0"));

        // when 2
        CreateReviewRequest req2 = new CreateReviewRequest();
        ReflectionTestUtils.setField(req2, "rating", new BigDecimal("3.0"));
        ReflectionTestUtils.setField(req2, "content", "볼만하네요.");
        reviewService.createReview(user2.getId(), movie.getId(), req2);

        // then 2: (5.0 + 3.0) / 2 = 4.0
        Movie updatedMovie2 = movieRepository.findById(movie.getId()).orElseThrow();
        assertThat(updatedMovie2.getReviewCount()).isEqualTo(2);
        assertThat(updatedMovie2.getRatingAvg()).isEqualByComparingTo(new BigDecimal("4.0"));

        // test like toggle
        boolean liked = reviewService.toggleReviewLike(user2.getId(), res1.getId());
        assertThat(liked).isTrue();
    }
}
