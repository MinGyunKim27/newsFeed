package org.example.newsfeed.global.common.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T> (
        List<T> content,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean isFirst,
        boolean isLast
) {
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }
}