package org.example.newsfeed.image.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.post.entity.Post;

/**
 * 이미지 정보를 나타내는 JPA 엔티티 클래스입니다.
 * 각 이미지 객체는 하나의 게시글(Post)에 연결될 수 있습니다.
 */
@Table(name = "image") // 매핑될 테이블 이름 설정
@Entity // JPA가 이 클래스를 엔티티로 인식하도록 지정
@Getter // Lombok을 통해 Getter 메서드 자동 생성
@Setter // Lombok을 통해 Setter 메서드 자동 생성
public class Image {

    /**
     * 이미지의 고유 ID (기본 키).
     * 자동으로 값이 생성됩니다.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 이미지의 URL을 저장하는 필드입니다.
     * - 최대 길이는 500자
     * - null을 허용하지 않음
     */
    @Column(name = "image_url", length = 500, nullable = false)
    private String imageUrl;

    /**
     * 이미지가 속한 게시글과의 연관관계 설정 (다대일).
     * - 지연 로딩: 실제 접근 시점에만 Post 객체를 불러옴
     * - 외래 키 이름은 post_id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 이미지 URL을 받아 초기화하는 생성자입니다.
     * 게시글(Post)과의 연관관계는 별도로 설정합니다.
     *
     * @param imageUrl 이미지의 경로 또는 URL
     */
    public Image(String imageUrl){
        this.imageUrl = imageUrl;
    }

    /**
     * 기본 생성자 (JPA 사용을 위해 반드시 필요).
     */
    public Image() {
    }
}
