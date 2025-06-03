package org.example.newsfeed.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 게시글 목록 조회 시 응답에 사용되는 DTO입니다.
 * 게시글 리스트와 페이징 정보를 함께 클라이언트에 전달합니다.
 */
@AllArgsConstructor
@Getter
public class PostListResponseDto {

    /**
     * 현재 페이지에 포함된 게시글 목록입니다.
     */
    private List<PostResponseDto> posts;

    /**
     * 전체 페이지 수입니다.
     */
    private int totalPages;

    /**
     * 현재 페이지 번호입니다. (0부터 시작)
     */
    private int currentPage;

    /**
     * Spring Data의 Page 객체를 기반으로 PostListResponseDto를 생성합니다.
     * 페이지에 포함된 게시글 목록, 전체 페이지 수, 현재 페이지 번호를 추출합니다.
     *
     * @param page PostResponseDto를 담고 있는 페이지 객체
     */
    public PostListResponseDto(Page<PostResponseDto> page) {
        this.posts = page.getContent();       // 현재 페이지에 포함된 게시글 목록
        this.totalPages = page.getTotalPages(); // 전체 페이지 수
        this.currentPage = page.getNumber();    // 현재 페이지 번호 (0-based)
    }
}
