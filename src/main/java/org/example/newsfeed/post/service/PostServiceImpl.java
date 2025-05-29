package org.example.newsfeed.post.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.exception.BaseException;
import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.dto.UpdatePostRequestDto;
import org.example.newsfeed.post.entitiy.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long createPost(CreatePostRequestDto dto, Long userId) {

        User user =userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        // 저장할 포스트 엔티티 생성
        Post post = Post.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .build();



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

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        // 본인 글만 지울 수 있게 확인
        if(!post.getUser().getId().equals(userId)) {
            throw new BaseException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        postRepository.delete(post);

    }

    @Override
    @Transactional

    // 페이지 구현, 생성시각을 기준으로 내림차순 정렬
    public Page<PostResponseDto> getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // 페이지 안에 있는 모든 post 꺼내고
        return postRepository.findAll(pageable)
                // 모든 post를 dto로 변환해서 넘겨주자
                .map(PostResponseDto::new);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, UpdatePostRequestDto dto, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new BaseException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        // 변경사항이 있다면 자동 반영
        post.update(dto.getTitle(), dto.getContent(), dto.getImageUrl());
    }


}
