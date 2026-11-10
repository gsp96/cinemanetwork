package com.cinemanetwork.domain.movie.repository;

import com.cinemanetwork.domain.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTmdbId(Long tmdbId);
    Optional<Movie> findByKobisMovieCd(String kobisMovieCd);
    Optional<Movie> findByTitle(String title);

    @Query("SELECT m FROM Movie m WHERE " +
           "(:query IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.director) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:genre IS NULL OR m.genres LIKE CONCAT('%', :genre, '%'))")
    Page<Movie> searchMovies(@Param("query") String query, @Param("genre") String genre, Pageable pageable);

    List<Movie> findTop10ByOrderByRatingAvgDesc();
}
