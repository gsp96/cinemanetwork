package com.cinemanetwork.domain.post.repository;

import com.cinemanetwork.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
    List<Comment> findByUserId(Long userId);
    int countByPostId(Long postId);
}
