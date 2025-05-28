package org.example.newsfeed.post.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

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

    // 이미지URL
    @Column(length = 500, nullable = true)
    private String imageUrl;

}
