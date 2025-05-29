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
import org.example.newsfeed.post.entitiy.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public CommentResponseDto createComment(String content, Long postId, Long userId) {

        // 유저데이터 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        
        // 게시글 조회
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Comment comment = new Comment(content, user, post);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Override
    public PagedResponse<CommentResponseDto> getCommentList(PageRequest pageRequest, Long postId) {
        Page<Comment> commentPage = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageRequest);
        Page<CommentResponseDto> dtoPage = commentPage.map(CommentResponseDto::new);
        return PagedResponse.from(dtoPage);
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(String content, Long commentId, Long userId) {

        // 유저데이터 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new NoPermissionException();
        }

        comment.updateContent(content);

        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {

        // 유저데이터 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new NoPermissionException();
        }



        commentRepository.delete(comment);
    }
}
