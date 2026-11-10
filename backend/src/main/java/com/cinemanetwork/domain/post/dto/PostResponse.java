package com.cinemanetwork.domain.post.dto;

import com.cinemanetwork.domain.post.entity.Post;
import com.cinemanetwork.domain.post.entity.PostCategory;
import com.cinemanetwork.domain.user.dto.UserProfileResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostResponse {
    private Long id;
    private PostCategory category;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer commentCount;
    private Boolean isSecret;
    private UserProfileResponse user;
    private List<CommentResponse> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse summaryFrom(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .viewCount(post.getViewCount())
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                .isSecret(post.getIsSecret())
                .user(UserProfileResponse.from(post.getUser()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostResponse detailFrom(Post post, List<CommentResponse> comments) {
        return PostResponse.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .commentCount(comments != null ? comments.size() : 0)
                .isSecret(post.getIsSecret())
                .user(UserProfileResponse.from(post.getUser()))
                .comments(comments)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
