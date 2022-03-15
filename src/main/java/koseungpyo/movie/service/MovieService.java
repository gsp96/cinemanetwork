package koseungpyo.movie.service;

import java.util.List;

import koseungpyo.movie.domain.Movie;

public interface MovieService {
	List<Movie> getMovies();
	Movie getMovie(int movieNum);
	void addMovie(Movie movie);
	void fixMovie(Movie movie);
	void delMovie(int movieNum);
}