package com.cinemanetwork.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다."),

    // User / Auth
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U001", "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U003", "사용자를 찾을 수 없습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "U004", "이메일 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "U005", "접근 권한이 없습니다."),

    // Movie
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "영화를 찾을 수 없습니다."),

    // Review
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "R001", "해당 영화에 이미 작성한 리뷰가 존재합니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R002", "리뷰를 찾을 수 없습니다."),

    // Post / Comment
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P002", "댓글을 찾을 수 없습니다."),
    SECRET_POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "P003", "비밀글은 작성자와 관리자만 조회할 수 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
