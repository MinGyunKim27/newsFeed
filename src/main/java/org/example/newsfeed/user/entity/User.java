package org.example.newsfeed.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.newsfeed.global.common.entity.BaseEntity;

/**
 * 사용자(User) 정보를 나타내는 JPA 엔티티 클래스입니다.
 * 회원가입, 로그인, 프로필 관리 등에 사용됩니다.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "users") // 테이블명은 "users"
public class User extends BaseEntity {

    /**
     * 사용자 고유 식별자 (Primary Key).
     * 자동 증가 전략 사용.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이름 (닉네임 등).
     * null 불가.
     */
    @Column(nullable = false)
    private String username;

    /**
     * 사용자 이메일 (로그인 ID 역할).
     * null 불가, 중복 불가 (unique).
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 비밀번호 (암호화된 상태로 저장됨).
     * null 불가.
     */
    @Column(nullable = false)
    private String password;

    /**
     * 프로필 이미지 URL (선택값).
     */
    private String profileImage;

    /**
     * 자기소개 (선택값).
     */
    private String bio;

    /**
     * 기본 생성자 외 생성자.
     * 회원가입 시 username, email, password만 필수로 사용.
     *
     * @param username 사용자 이름
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * 프로필 정보(username, 이미지, 자기소개)를 수정합니다.
     *
     * @param username 수정할 사용자 이름
     * @param profileImage 수정할 프로필 이미지 URL
     * @param bio 수정할 자기소개
     */
    public void updateProfile(String username, String profileImage, String bio) {
        this.username = username;
        this.profileImage = profileImage;
        this.bio = bio;
    }

    /**
     * 비밀번호를 수정합니다.
     *
     * @param newPassword 수정할 새 비밀번호
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
