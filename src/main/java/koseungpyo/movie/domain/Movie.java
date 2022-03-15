package koseungpyo.movie.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Movie {
	private int movieNum;
	private String title;
	private LocalDate openingDate;
	private String genre;
	private String directorName;
	private String mainActorName;
	private String posterFileName;
	private String audienceNum;
	private String topic;
}