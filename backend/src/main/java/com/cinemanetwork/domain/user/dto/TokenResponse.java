package com.cinemanetwork.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private UserProfileResponse user;

    public static TokenResponse of(String accessToken, String refreshToken, UserProfileResponse user) {
        return new TokenResponse(accessToken, refreshToken, "Bearer", user);
    }
}
