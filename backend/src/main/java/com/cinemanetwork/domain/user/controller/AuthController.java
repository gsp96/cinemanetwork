package com.cinemanetwork.domain.user.controller;

import com.cinemanetwork.domain.user.dto.LoginRequest;
import com.cinemanetwork.domain.user.dto.SignUpRequest;
import com.cinemanetwork.domain.user.dto.TokenResponse;
import com.cinemanetwork.domain.user.dto.UserProfileResponse;
import com.cinemanetwork.domain.user.service.UserService;
import com.cinemanetwork.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "01. Auth", description = "인증/인가 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserProfileResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        UserProfileResponse response = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 성공적으로 완료되었습니다.", response));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인되었습니다.", response));
    }
}
