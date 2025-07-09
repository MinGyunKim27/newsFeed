package org.example.newsfeed.user.dto;

import lombok.Setter;
import org.example.newsfeed.user.entity.User;
import lombok.Getter;

/**
 * 사용자 정보를 클라이언트에 응답할 때 사용하는 DTO 클래스입니다.
 * 민감한 정보(비밀번호 등)는 제외하고 필요한 정보만 전달합니다.
 */
@Getter // 모든 필드에 대한 getter 자동 생성
public class UserResponseDto {

    /**
     * 사용자 고유 ID
     */
    private final Long userId;

    /**
     * 사용자 이름 (닉네임 등)
     */
    private final String username;

    /**
     * 사용자 이메일 주소
     */
    private final String email;

    /**
     * 프로필 이미지 URL
     */
    private final String profileImage;

    /**
     * 사용자 소개글 (자기소개)
     */
    private final String bio;

    /**
     * 팔로워 수 (setter 필요하므로 별도 지정)
     */
    @Setter
    private Long followerCount;

    /**
     * User 엔티티로부터 필요한 정보를 추출하여 DTO에 매핑하는 생성자입니다.
     * @param user User 엔티티 객체
     */
    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.bio = user.getBio();
    }

    /**
     * User 엔티티를 UserResponseDto로 변환하는 정적 팩토리 메서드입니다.
     * 가독성과 일관성을 위해 사용됩니다.
     * @param user User 엔티티
     * @return 변환된 UserResponseDto 객체
     */
    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user);
    }
}
