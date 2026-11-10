package com.cinemanetwork.domain.post.entity;

import com.cinemanetwork.domain.user.entity.User;
import com.cinemanetwork.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PostCategory category;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private Boolean isSecret;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(User user, PostCategory category, String title, String content, Boolean isSecret) {
        this.user = user;
        this.category = category != null ? category : PostCategory.FREE;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.isSecret = isSecret != null ? isSecret : false;
    }

    public void update(String title, String content, PostCategory category, Boolean isSecret) {
        if (title != null && !title.isBlank()) this.title = title;
        if (content != null && !content.isBlank()) this.content = content;
        if (category != null) this.category = category;
        if (isSecret != null) this.isSecret = isSecret;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}
