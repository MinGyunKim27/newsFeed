package org.example.newsfeed.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.user.entity.User;

/**
 * 댓글(Comment) 엔티티 클래스입니다.
 * 댓글은 특정 게시글(Post)에 대해 작성자가 남긴 텍스트 정보입니다.
 * BaseEntity를 상속하여 생성일 및 수정일을 자동 관리합니다.
 */
@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    /**
     * 댓글 ID (기본 키)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 댓글 내용 (null 불가)
     */
    @Column(nullable = false)
    private String content;

    /**
     * 댓글 작성자 (User와 다대일 관계)
     * - 하나의 사용자는 여러 댓글을 작성할 수 있음
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 댓글이 작성된 게시글 (Post와 다대일 관계)
     * - 하나의 게시글에는 여러 댓글이 달릴 수 있음
     */
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * 댓글 생성자
     * @param content 댓글 내용
     * @param user 댓글 작성자
     * @param post 댓글이 작성된 게시글
     */
    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    /**
     * 댓글 내용을 수정합니다.
     * @param content 수정할 댓글 내용
     */
    public void updateContent(String content) {
        this.content = content;
    }
}
