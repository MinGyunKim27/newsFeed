package org.example.newsfeed.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // todo: 임시값 추후 user랑 연결
    private String userName;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long postId;

    public Comment(String content, String userName, Long userId, Long postId) {
        this.content = content;
        this.userName = userName;
        this.userId = userId;
        this.postId = postId;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
