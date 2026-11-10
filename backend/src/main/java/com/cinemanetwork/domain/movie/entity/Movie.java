package com.cinemanetwork.domain.movie.entity;

import com.cinemanetwork.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String kobisMovieCd;

    @Column(unique = true)
    private Long tmdbId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 200)
    private String originalTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String overview;

    private LocalDate releaseDate;

    @Column(length = 500)
    private String posterPath;

    @Column(length = 500)
    private String backdropPath;

    @Column(length = 200)
    private String genres;

    @Column(length = 100)
    private String director;

    @Column(length = 500)
    private String actors;

    private Integer runtime;

    @Column(precision = 3, scale = 1, nullable = false)
    private BigDecimal ratingAvg;

    @Column(nullable = false)
    private Integer reviewCount;

    @Builder
    public Movie(String kobisMovieCd, Long tmdbId, String title, String originalTitle,
                 String overview, LocalDate releaseDate, String posterPath, String backdropPath,
                 String genres, String director, String actors, Integer runtime) {
        this.kobisMovieCd = kobisMovieCd;
        this.tmdbId = tmdbId;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.genres = genres != null ? genres : "기타";
        this.director = director;
        this.actors = actors;
        this.runtime = runtime != null ? runtime : 0;
        this.ratingAvg = BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP);
        this.reviewCount = 0;
    }

    public void updateDetails(String overview, String posterPath, String backdropPath,
                              String genres, String director, String actors, Integer runtime) {
        if (overview != null && !overview.isBlank()) this.overview = overview;
        if (posterPath != null && !posterPath.isBlank()) this.posterPath = posterPath;
        if (backdropPath != null && !backdropPath.isBlank()) this.backdropPath = backdropPath;
        if (genres != null && !genres.isBlank()) this.genres = genres;
        if (director != null && !director.isBlank()) this.director = director;
        if (actors != null && !actors.isBlank()) this.actors = actors;
        if (runtime != null && runtime > 0) this.runtime = runtime;
    }

    public void setKobisMovieCd(String kobisMovieCd) {
        this.kobisMovieCd = kobisMovieCd;
    }

    public void updateRatingStats(BigDecimal newRatingAvg, int newReviewCount) {
        this.ratingAvg = newRatingAvg.setScale(1, RoundingMode.HALF_UP);
        this.reviewCount = newReviewCount;
    }
}
