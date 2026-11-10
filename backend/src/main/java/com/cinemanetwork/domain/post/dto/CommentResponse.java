package com.cinemanetwork.domain.post.dto;

import com.cinemanetwork.domain.post.entity.Comment;
import com.cinemanetwork.domain.user.dto.UserProfileResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private Long postId;
    private String content;
    private UserProfileResponse user;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .user(UserProfileResponse.from(comment.getUser()))
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
