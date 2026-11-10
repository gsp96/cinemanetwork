package com.cinemanetwork.domain.movie.service;

import com.cinemanetwork.domain.movie.dto.BoxOfficeResponse;
import com.cinemanetwork.domain.movie.dto.MovieResponse;
import com.cinemanetwork.domain.movie.entity.BoxOffice;
import com.cinemanetwork.domain.movie.entity.Movie;
import com.cinemanetwork.domain.movie.repository.BoxOfficeRepository;
import com.cinemanetwork.domain.movie.repository.MovieRepository;
import com.cinemanetwork.global.exception.CustomException;
import com.cinemanetwork.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final BoxOfficeRepository boxOfficeRepository;

    public List<BoxOfficeResponse> getLatestBoxOffice() {
        List<BoxOffice> boxOffices = boxOfficeRepository.findLatestBoxOfficeWithMovie();
        return boxOffices.stream()
                .map(BoxOfficeResponse::from)
                .collect(Collectors.toList());
    }

    public Page<MovieResponse> searchMovies(String query, String genre, Pageable pageable) {
        Page<Movie> movies = movieRepository.searchMovies(query, genre, pageable);
        return movies.map(MovieResponse::from);
    }

    public MovieResponse getMovieDetail(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
        return MovieResponse.from(movie);
    }

    public List<MovieResponse> getTopRatedMovies() {
        List<Movie> movies = movieRepository.findTop10ByOrderByRatingAvgDesc();
        return movies.stream()
                .map(MovieResponse::from)
                .collect(Collectors.toList());
    }

    public Movie getMovieEntity(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
    }
}
