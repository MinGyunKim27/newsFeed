package org.example.newsfeed.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.like.entity.Like;

/**
 * 좋아요 응답 DTO
 * 특정 게시글 또는 댓글에 대한 '좋아요' 정보를 클라이언트에 전달할 때 사용됩니다.
 */
@Getter
@NoArgsConstructor
public class LikeResponseDto {

    /** 좋아요 ID */
    private Long likeId;

    /** 좋아요를 누른 사용자 ID */
    private Long userId;

    /** 좋아요를 누른 사용자 이름 */
    private String userName;

    /** 사용자 프로필 이미지 URL */
    private String profileImage;

    /**
     * Like 엔티티를 기반으로 DTO 생성
     *
     * @param like Like 엔티티 객체
     */
    public LikeResponseDto(Like like) {
        this.likeId = like.getId();                            // 좋아요 ID 설정
        this.userId = like.getUser().getId();                  // 사용자 ID 설정
        this.userName = like.getUser().getUsername();          // 사용자 이름 설정
        this.profileImage = like.getUser().getProfileImage();  // 프로필 이미지 URL 설정
    }
}
