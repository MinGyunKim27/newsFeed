package org.example.newsfeed.post.service;

import lombok.RequiredArgsConstructor;
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
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글(Post) 관련 비즈니스 로직을 처리하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시글을 생성합니다.
     * - 사용자 ID로 사용자 조회
     * - 이미지 ID 목록으로 Image 엔티티 조회 후 Post와 연관관계 설정
     * - 게시글 저장
     *
     * @param dto     게시글 생성 요청 DTO
     * @param userId  작성자 ID
     * @return 생성된 게시글의 ID
     */
    @Override
    @Transactional
    public Long createPost(CreatePostRequestDto dto, Long userId) {
        List<Image> images = imageRepository.findAllById(dto.getImageIds());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        Post post = Post.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .images(images)
                .build();

        for (Image image : images) {
            image.setPost(post);
        }

        postRepository.save(post);
        return post.getId();
    }

    /**
     * 게시글 단건 조회
     *
     * @param postId 조회할 게시글 ID
     * @return 게시글 응답 DTO
     */
    @Override
    @Transactional
    public PostResponseDto findById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        List<Image> images = imageRepository.findAllByPost_Id(postId);

        return new PostResponseDto(post, images.stream()
                .map(image -> new ImageUploadResponseDto(image.getId(), image.getImageUrl()))
                .collect(Collectors.toList()));
    }

    /**
     * 게시글 삭제
     * - 작성자 본인만 삭제 가능
     * - 게시글 관련 댓글 및 좋아요도 함께 삭제
     *
     * @param postId  삭제할 게시글 ID
     * @param userId  요청자 ID
     */
    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new BaseException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        commentRepository.deleteAllByPost_Id(postId);
        likeRepository.deleteAllByPost_Id(postId);
        postRepository.delete(post);
    }

    /**
     * 전체 게시글 목록 조회 (페이지네이션)
     * - 생성일 기준 내림차순 정렬
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 게시글 페이지 응답
     */
    @Override
    @Transactional
    public Page<PostResponseDto> getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return postRepository.findAll(pageable)
                .map(post -> {
                    List<ImageUploadResponseDto> imageDtos = imageRepository.findAllByPost_Id(post.getId())
                            .stream()
                            .map(image -> new ImageUploadResponseDto(image.getId(), image.getImageUrl()))
                            .toList();

                    return new PostResponseDto(post, imageDtos);
                });
    }

    /**
     * 사용자별 게시글 목록 조회
     * - 정렬 조건: 최신순, 좋아요순, 팔로잉순(추후 구현)
     *
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @param userId 사용자 ID
     * @param sort   정렬 조건 ("latest", "recommended", "following")
     * @return 게시글 페이지 응답
     */
    public Page<PostResponseDto> getPostListByUser(int page, int size, Long userId, String sort) {
        Pageable pageable;

        switch (sort) {
            case "recommended":
                pageable = PageRequest.of(page, size, Sort.by("likeCount").descending());
                break;
            case "following":
                // 팔로잉은 향후 로직 추가 필요. 임시로 최신순
                pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                break;
            case "latest":
            default:
                pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                break;
        }

        return postRepository.findAllByUser_Id(userId, pageable)
                .map(post -> {
                    List<ImageUploadResponseDto> imageDtos = imageRepository.findAllByPost_Id(post.getId())
                            .stream()
                            .map(image -> new ImageUploadResponseDto(image.getId(), image.getImageUrl()))
                            .toList();

                    return new PostResponseDto(post, imageDtos);
                });
    }

    /**
     * 게시글 수정
     * - 작성자 본인만 수정 가능
     * - 이미지 연관관계 재설정
     *
     * @param postId 게시글 ID
     * @param dto    수정 요청 DTO
     * @param userId 요청자 ID
     */
    @Override
    @Transactional
    public void updatePost(Long postId, UpdatePostRequestDto dto, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new BaseException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        List<Image> images = imageRepository.findAllById(dto.getImageIds());

        for (Image image : images) {
            image.setPost(post);
        }

        post.update(dto.getTitle(), dto.getContent(), images);
    }
}
