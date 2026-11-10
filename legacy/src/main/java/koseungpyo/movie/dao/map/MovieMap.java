package koseungpyo.movie.dao.map;

import java.util.List;

import koseungpyo.movie.domain.Movie;

public interface MovieMap {
	List<Movie> selectMovies();
	Movie selectMovie(int movieNum);
	void insertMovie(Movie movie);
	void updateMovie(Movie movie);
	void deleteMovie(int movieNum);
}