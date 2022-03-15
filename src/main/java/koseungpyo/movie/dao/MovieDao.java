package koseungpyo.movie.dao;

import java.util.List;

import koseungpyo.movie.domain.Movie;

public interface MovieDao {
	List<Movie> selectMovies();
	Movie selectMovie(int movieNum);
	void insertMovie(Movie movie);
	void updateMovie(Movie movie);
	void deleteMovie(int movieNum);
}