package org.example.newsfeed.post.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.exception.BaseException;
import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.entitiy.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private  final PostRepository postRepository;

    @Override
    public Long createPost(CreatePostRequestDto dto, User user) {

        // 저장할 포스트 엔티티 생성
        Post post = Post.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .build();

        // 저장
        postRepository.save(post);

        // id값 반환
        return post.getId();
    }

    @Override
    @Transactional
    public PostResponseDto findById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));
        return new PostResponseDto(post);
    }
}
