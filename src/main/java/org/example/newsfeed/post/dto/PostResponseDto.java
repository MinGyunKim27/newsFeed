package org.example.newsfeed.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.newsfeed.image.dto.ImageUploadResponseDto;
import org.example.newsfeed.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글(Post)에 대한 응답 데이터를 클라이언트에 전달하기 위한 DTO 클래스입니다.
 * 게시글 본문뿐 아니라 작성자 정보, 이미지 목록 등을 포함합니다.
 */
@AllArgsConstructor
@Builder
@Getter
public class PostResponseDto {

    /**
     * 게시글 ID
     */
    private Long id;

    /**
     * 게시글 제목
     */
    private String title;

    /**
     * 게시글 본문 내용
     */
    private String content;

    /**
     * 게시글에 첨부된 이미지 목록
     */
    private List<ImageUploadResponseDto> images;

    /**
     * 작성자 이름 (username)
     */
    private String author;

    /**
     * 작성자의 프로필 이미지 URL
     */
    private String authorImageUrl;

    /**
     * 작성자 ID
     */
    private Long authorId;

    /**
     * 게시글 생성 시각
     */
    private LocalDateTime createdAt;

    /**
     * Post 엔티티와 이미지 응답 DTO 리스트를 기반으로 PostResponseDto를 생성하는 생성자입니다.
     *
     * @param post 게시글 엔티티
     * @param dto 게시글에 포함된 이미지 목록 (ImageUploadResponseDto 리스트)
     */
    public PostResponseDto(Post post, List<ImageUploadResponseDto> dto) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.images = dto;
        this.author = post.getUser().getUsername();
        this.authorImageUrl = post.getUser().getProfileImage();
        this.authorId = post.getUser().getId();
        this.createdAt = post.getCreatedAt();
    }
}
