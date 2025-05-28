package org.example.newsfeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto createComment(String content, Long postId) {

        // todo: 추후 로그인을 통해 id 값 설정
        Long tempUserId = 1L;

        // todo: 추후 UserId키값을 통해 작성자 맵핑


        Comment comment = new Comment(content, tempUserId, postId);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Override
    public Page<CommentResponseDto> getCommentList(PageRequest pageRequest, Long postId) {
        Page<Comment> commentPage = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageRequest);
        return commentPage.map(CommentResponseDto::new);
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(String content, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // todo: 추후 유저검증 예외처리

        comment.updateContent(content);

        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // todo: 추후 유저검증 예외처리

        commentRepository.delete(comment);
    }
}
