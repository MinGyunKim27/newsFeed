package org.example.newsfeed.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;
import org.example.newsfeed.user.entity.User;

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

    // 작성자
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    // 게시글
    @Column(nullable = false)
    private Long postId;

    public Comment(String content, User user, Long postId) {
        this.content = content;
        this.user = user;
        this.postId = postId;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
