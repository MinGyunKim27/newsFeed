package org.example.newsfeed.follow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;
import org.example.newsfeed.global.exception.BaseException;
import org.example.newsfeed.user.entity.User;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follows",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"follower_id", "following_id"})
        })
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    public Follow(User follower, User following) {
        validateFollow(follower, following);
        this.follower = follower;
        this.following = following;
    }

    public static Follow create(User follower, User following) {
        return new Follow(follower, following);
    }

    private void validateFollow(User follower, User following) {
        if (follower.getId().equals(following.getId())) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다.");
        }
    }

}
