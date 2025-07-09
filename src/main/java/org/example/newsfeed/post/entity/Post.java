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

/**
 * 게시글(Post) 엔티티
 * 사용자의 게시글 정보를 저장하며, 이미지와 연관됩니다.
 */
@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    /** 게시글 고유 ID (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 게시글 작성자 (User)와의 다대일 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // FK: user_id
    private User user;

    /** 게시글 제목 */
    @Column(length = 200, nullable = false)
    private String title;

    /** 게시글 내용 (텍스트 본문) */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 게시글에 포함된 이미지 리스트
     * - 양방향 연관관계: Image.post로 매핑됨
     * - cascade = ALL: 게시글 저장/삭제 시 이미지도 함께 처리됨
     * - orphanRemoval = true: 게시글에서 제거된 이미지는 DB에서도 삭제됨
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    /** 좋아요 수 */
    @Column(name = "like_count")
    private Long likeCount = 0L;

    /**
     * 게시글 수정 메서드
     * - 제목과 내용을 변경하고, 이미지 리스트를 갱신합니다.
     *
     * @param title 새 제목
     * @param content 새 내용
     * @param images 새 이미지 리스트
     */
    public void update(String title, String content, List<Image> images) {
        this.title = title;
        this.content = content;
        this.images.clear();           // 기존 이미지 제거
        this.images.addAll(images);    // 새 이미지 추가
    }
}
