package com.cinemanetwork.domain.movie.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class KobisApiClient {

    @Value("${external-api.kobis.key}")
    private String apiKey;

    @Value("${external-api.kobis.base-url}")
    private String baseUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public KobisApiClient(ObjectMapper objectMapper) {
        this.webClient = WebClient.builder().build();
        this.objectMapper = objectMapper;
    }

    public List<KobisBoxOfficeItem> fetchDailyBoxOffice(LocalDate targetDate) {
        String targetDateStr = targetDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String url = String.format("%s/boxoffice/searchDailyBoxOfficeList.json?key=%s&targetDt=%s",
                baseUrl, apiKey, targetDateStr);

        try {
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dailyList = rootNode.path("boxOfficeResult").path("dailyBoxOfficeList");

            List<KobisBoxOfficeItem> items = new ArrayList<>();
            if (dailyList.isArray() && !dailyList.isEmpty()) {
                for (JsonNode itemNode : dailyList) {
                    KobisBoxOfficeItem item = new KobisBoxOfficeItem();
                    item.setRank(itemNode.path("rank").asInt());
                    item.setMovieCd(itemNode.path("movieCd").asText());
                    item.setMovieNm(itemNode.path("movieNm").asText());
                    item.setOpenDt(itemNode.path("openDt").asText());
                    item.setAudiCnt(itemNode.path("audiCnt").asLong());
                    item.setAudiAcc(itemNode.path("audiAcc").asLong());
                    items.add(item);
                }
                log.info("KOBIS API로부터 {}개의 박스오피스 데이터를 수신했습니다.", items.size());
                return items;
            }
        } catch (Exception e) {
            log.warn("KOBIS API 호출 실패: {}. 기본 박스오피스 Mock 데이터를 사용합니다.", e.getMessage());
        }

        return getFallbackBoxOfficeItems();
    }

    private List<KobisBoxOfficeItem> getFallbackBoxOfficeItems() {
        List<KobisBoxOfficeItem> fallback = new ArrayList<>();
        String[][] sampleData = {
                {"1", "20234567", "파묘", "2024-02-22", "154320", "11910000"},
                {"2", "20237890", "범죄도시4", "2024-04-24", "132450", "11500000"},
                {"3", "20240123", "인사이드 아웃 2", "2024-06-12", "112300", "8790000"},
                {"4", "20239999", "베테랑2", "2024-09-13", "98400", "7520000"},
                {"5", "20231111", "듄: 파트2", "2024-02-28", "65400", "2010000"},
                {"6", "20232222", "서울의 봄", "2023-11-22", "54300", "13120000"},
                {"7", "20243333", "에이리언: 로물루스", "2024-08-14", "45200", "2000000"},
                {"8", "20244444", "파일럿", "2024-07-31", "34100", "4710000"},
                {"9", "20235555", "오펜하이머", "2023-08-15", "29800", "3230000"},
                {"10", "20246666", "데드풀과 울버린", "2024-07-24", "21000", "2510000"}
        };

        for (String[] data : sampleData) {
            KobisBoxOfficeItem item = new KobisBoxOfficeItem();
            item.setRank(Integer.parseInt(data[0]));
            item.setMovieCd(data[1]);
            item.setMovieNm(data[2]);
            item.setOpenDt(data[3]);
            item.setAudiCnt(Long.parseLong(data[4]));
            item.setAudiAcc(Long.parseLong(data[5]));
            fallback.add(item);
        }
        return fallback;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class KobisBoxOfficeItem {
        private int rank;
        private String movieCd;
        private String movieNm;
        private String openDt;
        private long audiCnt;
        private long audiAcc;
    }
}
