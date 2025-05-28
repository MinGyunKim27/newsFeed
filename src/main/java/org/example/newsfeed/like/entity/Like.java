package org.example.newsfeed.like.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;

@Entity
@Table(name = "`like`")
@NoArgsConstructor
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // todo: 추후 엔티티연결
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long postId;

    public Like(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
