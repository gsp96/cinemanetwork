package com.cinemanetwork.domain.user.service;

import com.cinemanetwork.domain.user.dto.LoginRequest;
import com.cinemanetwork.domain.user.dto.SignUpRequest;
import com.cinemanetwork.domain.user.dto.TokenResponse;
import com.cinemanetwork.domain.user.dto.UserProfileResponse;
import com.cinemanetwork.global.exception.CustomException;
import com.cinemanetwork.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 및 로그인 토큰 발급 테스트")
    void testSignUpAndLogin() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        ReflectionTestUtils.setField(signUpRequest, "email", "tester@example.com");
        ReflectionTestUtils.setField(signUpRequest, "password", "password123!");
        ReflectionTestUtils.setField(signUpRequest, "nickname", "테스터1호");

        // when
        UserProfileResponse profile = userService.signUp(signUpRequest);

        // then
        assertThat(profile).isNotNull();
        assertThat(profile.getEmail()).isEqualTo("tester@example.com");
        assertThat(profile.getNickname()).isEqualTo("테스터1호");

        // login
        LoginRequest loginRequest = new LoginRequest();
        ReflectionTestUtils.setField(loginRequest, "email", "tester@example.com");
        ReflectionTestUtils.setField(loginRequest, "password", "password123!");

        TokenResponse tokenResponse = userService.login(loginRequest);
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
        assertThat(tokenResponse.getRefreshToken()).isNotBlank();
        assertThat(tokenResponse.getUser().getNickname()).isEqualTo("테스터1호");
    }

    @Test
    @DisplayName("중복 이메일 가입 시 예외 발생")
    void testDuplicateEmail() {
        // given
        SignUpRequest req1 = new SignUpRequest();
        ReflectionTestUtils.setField(req1, "email", "dup@example.com");
        ReflectionTestUtils.setField(req1, "password", "pw1234");
        ReflectionTestUtils.setField(req1, "nickname", "유저A");
        userService.signUp(req1);

        SignUpRequest req2 = new SignUpRequest();
        ReflectionTestUtils.setField(req2, "email", "dup@example.com");
        ReflectionTestUtils.setField(req2, "password", "pw1234");
        ReflectionTestUtils.setField(req2, "nickname", "유저B");

        // then
        assertThatThrownBy(() -> userService.signUp(req2))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
    }
}
