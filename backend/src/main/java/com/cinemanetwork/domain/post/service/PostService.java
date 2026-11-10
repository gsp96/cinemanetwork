package com.cinemanetwork.domain.post.service;

import com.cinemanetwork.domain.post.dto.CommentResponse;
import com.cinemanetwork.domain.post.dto.CreateCommentRequest;
import com.cinemanetwork.domain.post.dto.CreatePostRequest;
import com.cinemanetwork.domain.post.dto.PostResponse;
import com.cinemanetwork.domain.post.entity.Comment;
import com.cinemanetwork.domain.post.entity.Post;
import com.cinemanetwork.domain.post.entity.PostCategory;
import com.cinemanetwork.domain.post.repository.CommentRepository;
import com.cinemanetwork.domain.post.repository.PostRepository;
import com.cinemanetwork.domain.user.entity.Role;
import com.cinemanetwork.domain.user.entity.User;
import com.cinemanetwork.domain.user.service.UserService;
import com.cinemanetwork.global.exception.CustomException;
import com.cinemanetwork.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Transactional
    public PostResponse createPost(Long userId, CreatePostRequest request) {
        User user = userService.getUserEntity(userId);

        Post post = Post.builder()
                .user(user)
                .category(request.getCategory())
                .title(request.getTitle())
                .content(request.getContent())
                .isSecret(Boolean.TRUE.equals(request.getIsSecret()))
                .build();

        Post saved = postRepository.save(post);
        return PostResponse.detailFrom(saved, List.of());
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> searchPosts(PostCategory category, String query, Pageable pageable) {
        Page<Post> posts = postRepository.searchPosts(category, query, pageable);
        return posts.map(PostResponse::summaryFrom);
    }

    @Transactional
    public PostResponse getPostDetail(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 비밀글 접근 제어: 작성자 또는 관리자만 조회 가능
        if (Boolean.TRUE.equals(post.getIsSecret())) {
            if (currentUserId == null) {
                throw new CustomException(ErrorCode.SECRET_POST_ACCESS_DENIED);
            }
            User currentUser = userService.getUserEntity(currentUserId);
            if (!post.getUser().getId().equals(currentUserId) && currentUser.getRole() != Role.ADMIN) {
                throw new CustomException(ErrorCode.SECRET_POST_ACCESS_DENIED);
            }
        }

        post.incrementViewCount();

        List<CommentResponse> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());

        return PostResponse.detailFrom(post, comments);
    }

    @Transactional
    public void deletePost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User currentUser = userService.getUserEntity(currentUserId);
        if (!post.getUser().getId().equals(currentUserId) && currentUser.getRole() != Role.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        postRepository.delete(post);
    }

    @Transactional
    public CommentResponse addComment(Long postId, Long currentUserId, CreateCommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        User currentUser = userService.getUserEntity(currentUserId);

        Comment comment = Comment.builder()
                .post(post)
                .user(currentUser)
                .content(request.getContent())
                .build();

        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved);
    }

    @Transactional
    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        User currentUser = userService.getUserEntity(currentUserId);
        if (!comment.getUser().getId().equals(currentUserId) && currentUser.getRole() != Role.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        commentRepository.delete(comment);
    }
}
