package org.example.newsfeed.like.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.user.entity.User;

/**
 * 게시글에 대한 '좋아요' 정보를 나타내는 JPA 엔티티입니다.
 * 사용자(User)와 게시글(Post) 간의 좋아요 관계를 관리합니다.
 */
@Entity
@Table(
        name = "`like`", // 테이블 이름을 백틱으로 감싸는 이유: like는 SQL 예약어이므로 충돌 방지
        uniqueConstraints = @UniqueConstraint( // 유저와 게시글 조합이 중복되지 않도록 제약 설정
                name = "uk_user_post",
                columnNames = {"user_id", "post_id"}
        )
)
@NoArgsConstructor // JPA를 위한 기본 생성자 생성 (Lombok)
@Getter // 모든 필드에 대한 getter 자동 생성 (Lombok)
public class Like extends BaseEntity {

    /**
     * 좋아요 고유 ID (기본 키).
     * AUTO_INCREMENT 방식으로 생성.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 좋아요를 누른 사용자와의 연관관계.
     * - 다대일 관계 (여러 좋아요가 하나의 사용자에 속함)
     * - 지연 로딩 적용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 좋아요가 눌린 게시글과의 연관관계.
     * - 다대일 관계 (여러 좋아요가 하나의 게시글에 속함)
     * - 지연 로딩 적용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * 사용자와 게시글을 기반으로 좋아요 객체를 생성하는 생성자입니다.
     *
     * @param user 좋아요를 누른 사용자
     * @param post 좋아요가 눌린 게시글
     */
    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
