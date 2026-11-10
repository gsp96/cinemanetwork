package com.cinemanetwork.domain.user.service;

import com.cinemanetwork.domain.user.dto.LoginRequest;
import com.cinemanetwork.domain.user.dto.SignUpRequest;
import com.cinemanetwork.domain.user.dto.TokenResponse;
import com.cinemanetwork.domain.user.dto.UserProfileResponse;
import com.cinemanetwork.domain.user.entity.Role;
import com.cinemanetwork.domain.user.entity.User;
import com.cinemanetwork.domain.user.repository.UserRepository;
import com.cinemanetwork.global.exception.CustomException;
import com.cinemanetwork.global.exception.ErrorCode;
import com.cinemanetwork.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public UserProfileResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .profileImageUrl(request.getProfileImageUrl())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("새로운 사용자가 가입했습니다: email={}, nickname={}", savedUser.getEmail(), savedUser.getNickname());
        return UserProfileResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = tokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId());

        return TokenResponse.of(accessToken, refreshToken, UserProfileResponse.from(user));
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserProfileResponse.from(user);
    }

    @Transactional(readOnly = true)
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 개발/테스트용 기본 관리자 및 테스트 유저 시드 데이터
        if (!userRepository.existsByEmail("admin@cinemanetwork.com")) {
            User admin = User.builder()
                    .email("admin@cinemanetwork.com")
                    .password(passwordEncoder.encode("admin1234"))
                    .nickname("관리자")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("기본 관리자 계정이 생성되었습니다: admin@cinemanetwork.com / admin1234");
        }

        if (!userRepository.existsByEmail("user@cinemanetwork.com")) {
            User testUser = User.builder()
                    .email("user@cinemanetwork.com")
                    .password(passwordEncoder.encode("user1234"))
                    .nickname("영화마니아")
                    .role(Role.USER)
                    .build();
            userRepository.save(testUser);
            log.info("테스트 사용자 계정이 생성되었습니다: user@cinemanetwork.com / user1234");
        }
    }
}
