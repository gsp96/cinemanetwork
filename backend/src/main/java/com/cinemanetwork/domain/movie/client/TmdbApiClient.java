package com.cinemanetwork.domain.movie.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class TmdbApiClient {

    @Value("${external-api.tmdb.key}")
    private String apiKey;

    @Value("${external-api.tmdb.base-url}")
    private String baseUrl;

    @Value("${external-api.tmdb.image-base-url}")
    private String imageBaseUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    // 폴백용 정적 포스터 및 메타데이터 맵
    private final Map<String, TmdbMovieDetail> sampleDetails = new HashMap<>();

    public TmdbApiClient(ObjectMapper objectMapper) {
        this.webClient = WebClient.builder().build();
        this.objectMapper = objectMapper;
        initSampleDetails();
    }

    public Optional<TmdbMovieDetail> searchMovieByTitle(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String url = String.format("%s/search/movie?api_key=%s&query=%s&language=ko-KR&page=1",
                    baseUrl, apiKey, encodedTitle);

            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode results = rootNode.path("results");

            if (results.isArray() && !results.isEmpty()) {
                JsonNode first = results.get(0);
                long tmdbId = first.path("id").asLong();
                String originalTitle = first.path("original_title").asText();
                String overview = first.path("overview").asText();
                String releaseDate = first.path("release_date").asText();
                String posterPath = first.path("poster_path").asText();
                String backdropPath = first.path("backdrop_path").asText();

                return Optional.of(TmdbMovieDetail.builder()
                        .tmdbId(tmdbId)
                        .title(title)
                        .originalTitle(originalTitle)
                        .overview(overview)
                        .releaseDate(releaseDate)
                        .posterUrl(posterPath.isEmpty() ? null : imageBaseUrl + "/w500" + posterPath)
                        .backdropUrl(backdropPath.isEmpty() ? null : imageBaseUrl + "/original" + backdropPath)
                        .genres("드라마, 스릴러")
                        .director("영화 감독")
                        .actors("주요 배우")
                        .runtime(125)
                        .build());
            }
        } catch (Exception e) {
            log.warn("TMDB API 호출 실패({}): {}. 샘플 메타데이터를 확인합니다.", title, e.getMessage());
        }

        return Optional.ofNullable(sampleDetails.get(title));
    }

    private void initSampleDetails() {
        sampleDetails.put("파묘", TmdbMovieDetail.builder()
                .tmdbId(938614L)
                .title("파묘")
                .originalTitle("Exhuma")
                .overview("미국 LA, 거액의 의뢰를 받은 무당 화림과 봉길은 기이한 병이 대물림되는 집안의 장손을 만난다. 조상의 묫자리가 화근임을 알아챈 화림은 이장을 권하고, 최고의 풍수사 상덕과 장의사 영근이 합류하는데...")
                .releaseDate("2024-02-22")
                .posterUrl("https://image.tmdb.org/t/p/w500/kZJjV8F4X4HnJ6l2PZfXjA7GfGq.jpg")
                .backdropUrl("https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?auto=format&fit=crop&w=1200&q=80")
                .genres("미스터리, 공포, 스릴러")
                .director("장재현")
                .actors("최민식, 김고은, 유해진, 이도현")
                .runtime(134)
                .build());

        sampleDetails.put("범죄도시4", TmdbMovieDetail.builder()
                .tmdbId(1011985L)
                .title("범죄도시4")
                .originalTitle("The Roundup: Punishment")
                .overview("신종 마약 사건 수사 3년 뒤, 괴물형사 마석도와 서울 광수대는 배달앱을 이용한 마약 판매 사건을 수사하던 중 수배 중인 앱 개발자가 필리핀에서 사망한 사건을 접하게 된다.")
                .releaseDate("2024-04-24")
                .posterUrl("https://image.tmdb.org/t/p/w500/b13a8l0n2v90hK8uL27F0W8xJ.jpg")
                .backdropUrl("https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=1200&q=80")
                .genres("액션, 범죄")
                .director("허명행")
                .actors("마동석, 김무열, 박지환, 이동휘")
                .runtime(109)
                .build());

        sampleDetails.put("인사이드 아웃 2", TmdbMovieDetail.builder()
                .tmdbId(1022789L)
                .title("인사이드 아웃 2")
                .originalTitle("Inside Out 2")
                .overview("13살이 된 라일리의 행복을 위해 매일 바쁘게 머릿속 감정 컨트롤 본부를 운영하는 기쁨, 슬픔, 버럭, 까칠, 소심. 그러던 어느 날, 낯선 감정인 불안, 당황, 따분, 부럽이 본부에 등장하는데!")
                .releaseDate("2024-06-12")
                .posterUrl("https://image.tmdb.org/t/p/w500/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg")
                .backdropUrl("https://images.unsplash.com/photo-1536440136628-849c177e76a1?auto=format&fit=crop&w=1200&q=80")
                .genres("애니메이션, 가족, 모험, 코미디")
                .director("켈시 만")
                .actors("에이미 폴러, 마야 호크, 켄싱턴 탈만")
                .runtime(96)
                .build());

        sampleDetails.put("듄: 파트2", TmdbMovieDetail.builder()
                .tmdbId(693134L)
                .title("듄: 파트2")
                .originalTitle("Dune: Part Two")
                .overview("황제의 모략으로 멸문한 가문의 유일한 후계자 폴. 어머니 레이디 제시카와 함께 사막으로 도망친 그는 그곳에서 만난 반란군들과 숨어 지내다 황제의 모든 것을 파괴할 전투를 준비한다.")
                .releaseDate("2024-02-28")
                .posterUrl("https://image.tmdb.org/t/p/w500/czembW0Rk1Ke7lCJGhkAiHVQ9VJ.jpg")
                .backdropUrl("https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=1200&q=80")
                .genres("SF, 모험, 액션")
                .director("드니 빌뇌브")
                .actors("티모시 샬라메, 젠데이아, 레베카 페르구손")
                .runtime(166)
                .build());

        sampleDetails.put("서울의 봄", TmdbMovieDetail.builder()
                .tmdbId(945961L)
                .title("서울의 봄")
                .originalTitle("12.12: The Day")
                .overview("1979년 12월 12일, 수도 서울에서 군사반란이 일어난다. 그날 밤, 대한민국 역사의 흐름을 바꾼 일촉즉발의 9시간이 펼쳐진다.")
                .releaseDate("2023-11-22")
                .posterUrl("https://image.tmdb.org/t/p/w500/7I6VUdPj6tQwhd19Blajyp2d50r.jpg")
                .backdropUrl("https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?auto=format&fit=crop&w=1200&q=80")
                .genres("드라마, 스릴러")
                .director("김성수")
                .actors("황정민, 정우성, 이성민, 박해준")
                .runtime(141)
                .build());
    }

    @Getter
    @Builder
    public static class TmdbMovieDetail {
        private Long tmdbId;
        private String title;
        private String originalTitle;
        private String overview;
        private String releaseDate;
        private String posterUrl;
        private String backdropUrl;
        private String genres;
        private String director;
        private String actors;
        private Integer runtime;
    }
}
