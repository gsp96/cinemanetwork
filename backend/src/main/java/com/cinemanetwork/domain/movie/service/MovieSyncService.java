package com.cinemanetwork.domain.movie.service;

import com.cinemanetwork.domain.movie.client.KobisApiClient;
import com.cinemanetwork.domain.movie.client.TmdbApiClient;
import com.cinemanetwork.domain.movie.entity.BoxOffice;
import com.cinemanetwork.domain.movie.entity.Movie;
import com.cinemanetwork.domain.movie.repository.BoxOfficeRepository;
import com.cinemanetwork.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieSyncService {

    private final KobisApiClient kobisApiClient;
    private final TmdbApiClient tmdbApiClient;
    private final MovieRepository movieRepository;
    private final BoxOfficeRepository boxOfficeRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (boxOfficeRepository.count() == 0) {
            log.info("초기 박스오피스 데이터가 존재하지 않아 자동 동기화를 시작합니다.");
            syncDailyBoxOffice(LocalDate.now().minusDays(1));
        }
    }

    @Scheduled(cron = "0 0 6 * * *") // 매일 아침 6시 실행
    public void scheduledBoxOfficeSync() {
        log.info("정기 박스오피스 스케줄러 실행");
        syncDailyBoxOffice(LocalDate.now().minusDays(1));
    }

    @Transactional
    public int syncDailyBoxOffice(LocalDate targetDate) {
        log.info("{} 기준 일일 박스오피스 동기화 시작...", targetDate);
        List<KobisApiClient.KobisBoxOfficeItem> items = kobisApiClient.fetchDailyBoxOffice(targetDate);

        int count = 0;
        for (KobisApiClient.KobisBoxOfficeItem item : items) {
            // 1. 영화 엔티티 찾기 또는 생성
            Movie movie = movieRepository.findByTitle(item.getMovieNm())
                    .orElseGet(() -> createMovieFromTmdb(item));

            if (movie.getKobisMovieCd() == null) {
                movie.setKobisMovieCd(item.getMovieCd());
            }

            // 2. 박스오피스 엔티티 찾기 또는 생성
            Optional<BoxOffice> boxOfficeOpt = boxOfficeRepository.findByTargetDateAndRankNum(targetDate, item.getRank());
            if (boxOfficeOpt.isPresent()) {
                BoxOffice bo = boxOfficeOpt.get();
                bo.updateRankData(item.getRank(), item.getAudiCnt(), item.getAudiAcc());
            } else {
                BoxOffice bo = BoxOffice.builder()
                        .movie(movie)
                        .targetDate(targetDate)
                        .rankNum(item.getRank())
                        .audiCnt(item.getAudiCnt())
                        .audiAcc(item.getAudiAcc())
                        .build();
                boxOfficeRepository.save(bo);
            }
            count++;
        }

        log.info("{} 기준 박스오피스 {}건 동기화 완료.", targetDate, count);
        return count;
    }

    private Movie createMovieFromTmdb(KobisApiClient.KobisBoxOfficeItem item) {
        Optional<TmdbApiClient.TmdbMovieDetail> detailOpt = tmdbApiClient.searchMovieByTitle(item.getMovieNm());

        if (detailOpt.isPresent()) {
            TmdbApiClient.TmdbMovieDetail detail = detailOpt.get();
            LocalDate releaseDate = null;
            try {
                if (detail.getReleaseDate() != null && !detail.getReleaseDate().isBlank()) {
                    releaseDate = LocalDate.parse(detail.getReleaseDate());
                }
            } catch (Exception ignored) {}

            Movie movie = Movie.builder()
                    .kobisMovieCd(item.getMovieCd())
                    .tmdbId(detail.getTmdbId())
                    .title(item.getMovieNm())
                    .originalTitle(detail.getOriginalTitle())
                    .overview(detail.getOverview())
                    .releaseDate(releaseDate)
                    .posterPath(detail.getPosterUrl())
                    .backdropPath(detail.getBackdropUrl())
                    .genres(detail.getGenres())
                    .director(detail.getDirector())
                    .actors(detail.getActors())
                    .runtime(detail.getRuntime())
                    .build();

            return movieRepository.save(movie);
        } else {
            // TMDB 정보가 없을 경우 기본값으로 생성
            Movie movie = Movie.builder()
                    .kobisMovieCd(item.getMovieCd())
                    .title(item.getMovieNm())
                    .overview("상세 정보 준비 중입니다.")
                    .genres("영화")
                    .build();
            return movieRepository.save(movie);
        }
    }
}
