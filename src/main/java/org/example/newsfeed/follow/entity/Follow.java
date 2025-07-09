package org.example.newsfeed.follow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;
import org.example.newsfeed.global.exception.DoNotFollowMySelf;
import org.example.newsfeed.user.entity.User;

/**
 * 사용자 간 팔로우 관계를 나타내는 엔티티 클래스입니다.
 * 팔로워(follower)와 팔로잉(following) 간의 관계를 저장합니다.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "follows",
        uniqueConstraints = {
                // 동일한 팔로워-팔로잉 쌍이 중복 저장되지 않도록 제약조건 설정
                @UniqueConstraint(columnNames = {"follower_id", "following_id"})
        })
public class Follow extends BaseEntity {

    /**
     * Follow 엔티티의 기본 키입니다. 자동으로 증가합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 팔로우를 하는 사용자(follower)입니다.
     * User 엔티티와 다대일 관계이며, 지연 로딩(LAZY) 전략을 사용합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    /**
     * 팔로우 당하는 사용자(following)입니다.
     * User 엔티티와 다대일 관계이며, 지연 로딩(LAZY) 전략을 사용합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    /**
     * Follow 객체를 생성합니다. 자기 자신을 팔로우할 수 없도록 검증합니다.
     *
     * @param follower 팔로우를 하는 사용자
     * @param following 팔로우 당하는 사용자
     */
    public Follow(User follower, User following) {
        validateFollow(follower, following);
        this.follower = follower;
        this.following = following;
    }

    /**
     * Follow 객체를 생성하는 정적 팩토리 메서드입니다.
     *
     * @param follower 팔로우를 하는 사용자
     * @param following 팔로우 당하는 사용자
     * @return Follow 객체
     */
    public static Follow create(User follower, User following) {
        return new Follow(follower, following);
    }

    /**
     * 팔로우 유효성 검사를 수행합니다.
     * 자기 자신을 팔로우하려는 경우 예외를 발생시킵니다.
     *
     * @param follower 팔로우를 하는 사용자
     * @param following 팔로우 당하는 사용자
     */
    private void validateFollow(User follower, User following) {
        if (follower.getId().equals(following.getId())) {
            throw new DoNotFollowMySelf();
        }
    }

}
