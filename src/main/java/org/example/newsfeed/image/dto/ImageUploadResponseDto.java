package org.example.newsfeed.image.dto;

/**
 * 이미지 업로드 응답 DTO
 * 이미지 업로드 후 클라이언트에게 반환할 이미지 ID와 URL을 담는 데이터 구조입니다.
 *
 * Java record를 사용하여 불변 객체로 간단하게 정의됩니다.
 *
 * @param imageId  업로드된 이미지의 고유 ID
 * @param imageUrl 업로드된 이미지의 접근 가능한 URL
 */
public record ImageUploadResponseDto(Long imageId, String imageUrl) {
}
