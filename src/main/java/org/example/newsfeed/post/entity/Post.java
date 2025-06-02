package org.example.newsfeed.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.global.common.entity.BaseEntity;
import org.example.newsfeed.image.entity.Image;
import org.example.newsfeed.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY) // N 대 1 매칭, 성능 향상을 위해 LAZY 로딩
    @JoinColumn(name = "user_id", nullable = false) // post 테이블에서 fk user_id를 통해 user 테이블과 연결
    private User user;

    // 제목
    @Column(length = 200, nullable = false)
    private String title;

    // 내용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Column(name = "like_count")
    private Long likeCount = 0L;  // 좋아요 수 저장 필드

    public void update(String title, String content, List<Image> images) {
        this.title = title;
        this.content = content;
        this.images.clear(); // 기존 이미지 제거
        this.images.addAll(images); // 새 이미지 추가
    }


}
