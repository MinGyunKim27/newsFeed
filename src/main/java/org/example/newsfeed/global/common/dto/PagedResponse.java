package org.example.newsfeed.global.common.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 페이징된 응답을 표현하는 공통 DTO입니다.
 * 주로 클라이언트에게 리스트 데이터를 페이지 단위로 응답할 때 사용됩니다.
 *
 * @param <T> 페이지에 담길 개별 요소 타입
 */
public record PagedResponse<T> (
        List<T> content,       // 현재 페이지에 포함된 데이터 리스트
        int page,              // 현재 페이지 번호 (0부터 시작)
        int size,              // 페이지당 항목 수
        int totalPages,        // 전체 페이지 수
        long totalElements,    // 전체 항목 수
        boolean isFirst,       // 첫 페이지 여부
        boolean isLast         // 마지막 페이지 여부
) {
    /**
     * Spring Data의 Page 객체로부터 PagedResponse 객체를 생성합니다.
     *
     * @param page Page<T> 객체
     * @return 변환된 PagedResponse<T> 객체
     */
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),      // 현재 페이지의 실제 데이터
                page.getNumber(),       // 현재 페이지 번호
                page.getSize(),         // 페이지 크기
                page.getTotalPages(),   // 총 페이지 수
                page.getTotalElements(),// 전체 요소 수
                page.isFirst(),         // 첫 페이지 여부
                page.isLast()           // 마지막 페이지 여부
        );
    }
}
