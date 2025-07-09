package org.example.newsfeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.example.newsfeed.global.common.dto.PagedResponse;
import org.example.newsfeed.global.exception.CommentNotFoundException;
import org.example.newsfeed.global.exception.NoPermissionException;
import org.example.newsfeed.global.exception.PostNotFoundException;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 댓글 서비스 구현 클래스
 * 댓글 생성, 조회, 수정, 삭제 기능 제공
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 생성
     *
     * @param content 댓글 내용
     * @param postId 댓글이 달릴 게시글 ID
     * @param userId 댓글 작성자 ID
     * @return 생성된 댓글 DTO
     */
    @Override
    public CommentResponseDto createComment(String content, Long postId, Long userId) {

        // 유저 정보 조회 (존재하지 않으면 예외 발생)
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 게시글 정보 조회 (존재하지 않으면 예외 발생)
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        // 댓글 생성 및 저장
        Comment comment = new Comment(content, user, post);
        commentRepository.save(comment);

        // 저장된 댓글을 DTO로 변환하여 반환
        return new CommentResponseDto(comment);
    }

    /**
     * 게시글에 달린 댓글 목록 조회 (페이징 지원)
     *
     * @param pageRequest 페이징 정보
     * @param postId 게시글 ID
     * @return 댓글 목록 DTO 페이지
     */
    @Override
    public PagedResponse<CommentResponseDto> getCommentList(PageRequest pageRequest, Long postId) {
        // 게시글의 댓글 목록을 최신순으로 조회
        Page<Comment> commentPage = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageRequest);

        // 엔티티 페이지를 DTO 페이지로 변환
        Page<CommentResponseDto> dtoPage = commentPage.map(CommentResponseDto::new);

        return PagedResponse.from(dtoPage);
    }

    /**
     * 게시글의 전체 댓글 개수 반환
     *
     * @param postId 게시글 ID
     * @return 댓글 수
     */
    @Override
    public Long getCommentTotalCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    /**
     * 댓글 수정
     *
     * @param content 수정할 댓글 내용
     * @param commentId 댓글 ID
     * @param userId 요청 유저 ID
     * @return 수정된 댓글 DTO
     */
    @Transactional
    @Override
    public CommentResponseDto updateComment(String content, Long commentId, Long userId) {

        // 유저 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        // 댓글 작성자와 요청자가 일치하는지 확인
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new NoPermissionException();
        }

        // 댓글 내용 업데이트
        comment.updateContent(content);

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 ID
     * @param userId 요청 유저 ID
     */
    @Override
    public void deleteComment(Long commentId, Long userId) {

        // 유저 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        // 댓글 작성자와 요청자가 일치하는지 확인
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new NoPermissionException();
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }
}
