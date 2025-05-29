package org.example.newsfeed.like.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;
import org.example.newsfeed.post.entitiy.Post;
import org.example.newsfeed.user.entity.User;

@Entity
@Table(name = "`like`")
@NoArgsConstructor
@Getter
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    // 게시글
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
