package org.example.newsfeed.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.comment.entity.Comment;

import java.time.LocalDateTime;

/**
 * 댓글 응답 DTO 클래스.
 * 클라이언트에게 댓글 데이터를 전달할 때 사용됩니다.
 */
@Getter
@NoArgsConstructor
public class CommentResponseDto {

    /** 댓글 ID */
    private Long id;

    /** 댓글 작성자 ID */
    private Long userId;

    /** 댓글 작성자 이름 */
    private String name;

    /** 댓글 내용 */
    private String content;

    /** 댓글 작성 시각 */
    private LocalDateTime createdAt;

    /**
     * 엔티티 객체를 기반으로 DTO를 생성하는 생성자.
     *
     * @param comment 댓글 엔티티
     */
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();                          // 댓글 ID 설정
        this.userId = comment.getUser().getId();            // 작성자 ID 설정
        this.name = comment.getUser().getUsername();        // 작성자 이름 설정
        this.content = comment.getContent();                // 댓글 내용 설정
        this.createdAt = comment.getCreatedAt();            // 댓글 작성 시각 설정
    }
}
