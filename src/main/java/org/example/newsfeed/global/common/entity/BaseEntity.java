package org.example.newsfeed.global.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 엔티티 공통 속성 정의 클래스 (추상 클래스)
 * 생성일자(createdAt), 수정일자(updatedAt)를 모든 엔티티에 자동으로 포함시킬 수 있도록 지원합니다.
 *
 * @MappedSuperclass: 이 클래스를 상속받는 엔티티에 필드를 포함시킵니다.
 * @EntityListeners: JPA Auditing 기능을 활성화합니다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * 생성 시간
     * 엔티티가 처음 저장될 때 자동으로 세팅됩니다.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 마지막 수정 시간
     * 엔티티가 수정될 때마다 자동으로 갱신됩니다.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
