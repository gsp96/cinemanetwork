package com.cinemanetwork.domain.post.repository;

import com.cinemanetwork.domain.post.entity.Post;
import com.cinemanetwork.domain.post.entity.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:query IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Post> searchPosts(@Param("category") PostCategory category, @Param("query") String query, Pageable pageable);

    Page<Post> findByUserId(Long userId, Pageable pageable);
}
