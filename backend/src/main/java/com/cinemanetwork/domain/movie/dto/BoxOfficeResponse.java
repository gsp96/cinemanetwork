package com.cinemanetwork.domain.movie.dto;

import com.cinemanetwork.domain.movie.entity.BoxOffice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BoxOfficeResponse {
    private Long id;
    private Integer rank;
    private LocalDate targetDate;
    private Long audiCnt;
    private Long audiAcc;
    private MovieResponse movie;

    public static BoxOfficeResponse from(BoxOffice boxOffice) {
        return BoxOfficeResponse.builder()
                .id(boxOffice.getId())
                .rank(boxOffice.getRankNum())
                .targetDate(boxOffice.getTargetDate())
                .audiCnt(boxOffice.getAudiCnt())
                .audiAcc(boxOffice.getAudiAcc())
                .movie(MovieResponse.from(boxOffice.getMovie()))
                .build();
    }
}
