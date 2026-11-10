package com.cinemanetwork.domain.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "box_offices", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"targetDate", "rankNum"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoxOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(nullable = false)
    private LocalDate targetDate;

    @Column(nullable = false)
    private Integer rankNum;

    private Long audiCnt;

    private Long audiAcc;

    @Builder
    public BoxOffice(Movie movie, LocalDate targetDate, Integer rankNum, Long audiCnt, Long audiAcc) {
        this.movie = movie;
        this.targetDate = targetDate;
        this.rankNum = rankNum;
        this.audiCnt = audiCnt != null ? audiCnt : 0L;
        this.audiAcc = audiAcc != null ? audiAcc : 0L;
    }

    public void updateRankData(Integer rankNum, Long audiCnt, Long audiAcc) {
        this.rankNum = rankNum;
        this.audiCnt = audiCnt;
        this.audiAcc = audiAcc;
    }
}
