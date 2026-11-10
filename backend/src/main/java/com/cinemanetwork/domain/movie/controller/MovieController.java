package com.cinemanetwork.domain.movie.controller;

import com.cinemanetwork.domain.movie.dto.BoxOfficeResponse;
import com.cinemanetwork.domain.movie.dto.MovieResponse;
import com.cinemanetwork.domain.movie.service.MovieService;
import com.cinemanetwork.domain.movie.service.MovieSyncService;
import com.cinemanetwork.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "03. Movie", description = "영화 및 박스오피스 API")
@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final MovieSyncService movieSyncService;

    @Operation(summary = "최신 박스오피스 TOP 10 조회")
    @GetMapping("/boxoffice")
    public ResponseEntity<ApiResponse<List<BoxOfficeResponse>>> getBoxOffice() {
        List<BoxOfficeResponse> response = movieService.getLatestBoxOffice();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "영화 목록 검색 및 필터링 (페이징)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MovieResponse>>> searchMovies(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @PageableDefault(size = 12, sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MovieResponse> response = movieService.searchMovies(query, genre, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "영화 상세 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieDetail(@PathVariable Long id) {
        MovieResponse response = movieService.getMovieDetail(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "평점 랭킹 추천 영화 TOP 10")
    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getTopRatedMovies() {
        List<MovieResponse> response = movieService.getTopRatedMovies();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "박스오피스 수동 동기화 (관리자용)")
    @PostMapping("/sync")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> syncBoxOffice(
            @RequestParam(required = false) String date) {
        LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now().minusDays(1);
        int syncedCount = movieSyncService.syncDailyBoxOffice(targetDate);
        return ResponseEntity.ok(ApiResponse.success("동기화가 완료되었습니다.", syncedCount));
    }
}
