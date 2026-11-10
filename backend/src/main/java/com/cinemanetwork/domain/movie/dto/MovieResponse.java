package com.cinemanetwork.domain.movie.dto;

import com.cinemanetwork.domain.movie.entity.Movie;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class MovieResponse {
    private Long id;
    private String kobisMovieCd;
    private Long tmdbId;
    private String title;
    private String originalTitle;
    private String overview;
    private LocalDate releaseDate;
    private String posterPath;
    private String backdropPath;
    private String genres;
    private String director;
    private String actors;
    private Integer runtime;
    private BigDecimal ratingAvg;
    private Integer reviewCount;

    public static MovieResponse from(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .kobisMovieCd(movie.getKobisMovieCd())
                .tmdbId(movie.getTmdbId())
                .title(movie.getTitle())
                .originalTitle(movie.getOriginalTitle())
                .overview(movie.getOverview())
                .releaseDate(movie.getReleaseDate())
                .posterPath(movie.getPosterPath())
                .backdropPath(movie.getBackdropPath())
                .genres(movie.getGenres())
                .director(movie.getDirector())
                .actors(movie.getActors())
                .runtime(movie.getRuntime())
                .ratingAvg(movie.getRatingAvg())
                .reviewCount(movie.getReviewCount())
                .build();
    }
}
