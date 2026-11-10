package com.cinemanetwork.domain.movie.repository;

import com.cinemanetwork.domain.movie.entity.BoxOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BoxOfficeRepository extends JpaRepository<BoxOffice, Long> {
    List<BoxOffice> findByTargetDateOrderByRankNumAsc(LocalDate targetDate);
    Optional<BoxOffice> findByTargetDateAndRankNum(LocalDate targetDate, Integer rankNum);

    @Query("SELECT DISTINCT b.targetDate FROM BoxOffice b ORDER BY b.targetDate DESC")
    List<LocalDate> findDistinctTargetDates();

    @Query("SELECT b FROM BoxOffice b JOIN FETCH b.movie WHERE b.targetDate = " +
           "(SELECT MAX(b2.targetDate) FROM BoxOffice b2) ORDER BY b.rankNum ASC")
    List<BoxOffice> findLatestBoxOfficeWithMovie();
}
