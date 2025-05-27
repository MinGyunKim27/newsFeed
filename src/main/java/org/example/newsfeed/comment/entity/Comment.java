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

    // 임시값 추후 user랑 연결
    private String userName;

    public Comment(String content, String userName) {
        this.content = content;
        this.userName = userName;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
