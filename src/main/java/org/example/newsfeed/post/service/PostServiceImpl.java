package org.example.newsfeed.post.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.example.newsfeed.global.exception.BaseException;
import org.example.newsfeed.image.dto.ImageUploadResponseDto;
import org.example.newsfeed.image.entity.Image;
import org.example.newsfeed.image.repository.ImageRepository;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.dto.UpdatePostRequestDto;
import org.example.newsfeed.post.entity.Post;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Long createPost(CreatePostRequestDto dto, Long userId) {

        // 이미지 ID 리스트로 Image 엔티티를 조회해서 post와 연관관계 설정
        List<Image> images = imageRepository.findAllById(dto.getImageIds());

        User user =userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        // 저장할 포스트 엔티티 생성
        Post post = Post.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .images(images)
                .build();

        for (Image image : images) {
            image.setPost(post); // 연관관계 설정
        }

        postRepository.save(post);

        // id값 반환
        return post.getId();
    }

    @Override
    @Transactional
    public PostResponseDto findById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        // 이미지 ID 리스트로 Image 엔티티를 조회해서 post와 연관관계 설정
        List<Image> images = imageRepository.findAllByPost_Id(postId);

        return new PostResponseDto(post,images.stream()
                .map(image -> new ImageUploadResponseDto(image.getId(), image.getImageUrl()))
                .collect(Collectors.toList()));
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

        commentRepository.deleteAllByPost_Id(postId);
        likeRepository.deleteAllByPost_Id(postId);

        postRepository.delete(post);

    }

    @Override
    @Transactional

    // 페이지 구현, 생성시각을 기준으로 내림차순 정렬
    public Page<PostResponseDto> getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // 페이지 안에 있는 모든 post 꺼내고
        return postRepository.findAll(pageable)
                .map(post -> {
                    List<ImageUploadResponseDto> imageDtos = imageRepository.findAllByPost_Id(post.getId())
                            .stream()
                            .map(image -> new ImageUploadResponseDto(image.getId(), image.getImageUrl()))
                            .toList();

                    return new PostResponseDto(post, imageDtos);
                });

    }

    public Page<PostResponseDto> getPostListByUser(int page, int size, Long userId, String sort) {
        Pageable pageable;

        switch(sort) {
            case "latest":
                pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                break;
            case "recommended":
                // 좋아요 수 기준 정렬 (좋아요 수가 많은 순)
                pageable = PageRequest.of(page, size, Sort.by("likeCount").descending());
                break;
            case "following":
                // 팔로잉 기능은 별도 메서드로 처리하거나, 일단 최신순으로
                pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                break;
            default:
                // 기본값은 최신순
                pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                break;
        }

        return postRepository.findAllByUser_Id(userId,pageable)
                .map(post -> {
                    List<ImageUploadResponseDto> imageDtos = imageRepository.findAllByPost_Id(post.getId())
                            .stream()
                            .map(image -> new ImageUploadResponseDto(image.getId(), image.getImageUrl()))
                            .toList();

                    return new PostResponseDto(post, imageDtos);
                });

    }



    @Override
    @Transactional
    public void updatePost(Long postId, UpdatePostRequestDto dto, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new BaseException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        // 이미지 ID 리스트로 Image 엔티티를 조회해서 post와 연관관계 설정
        List<Image> images = imageRepository.findAllById(dto.getImageIds());

        for (Image image : images) {
            image.setPost(post); // 연관관계 설정
        }

        // 변경사항이 있다면 자동 반영
        post.update(dto.getTitle(), dto.getContent(), images);
    }


}
