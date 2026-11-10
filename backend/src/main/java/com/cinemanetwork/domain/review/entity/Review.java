package com.cinemanetwork.domain.review.entity;

import com.cinemanetwork.domain.movie.entity.Movie;
import com.cinemanetwork.domain.user.entity.User;
import com.cinemanetwork.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "movie_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(precision = 2, scale = 1, nullable = false)
    private BigDecimal rating; // 0.5 ~ 5.0

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Integer likesCount;

    @Builder
    public Review(User user, Movie movie, BigDecimal rating, String content) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.content = content;
        this.likesCount = 0;
    }

    public void update(BigDecimal rating, String content) {
        if (rating != null) this.rating = rating;
        if (content != null && !content.isBlank()) this.content = content;
    }

    public void incrementLikes() {
        this.likesCount++;
    }

    public void decrementLikes() {
        if (this.likesCount > 0) {
            this.likesCount--;
        }
    }
}
