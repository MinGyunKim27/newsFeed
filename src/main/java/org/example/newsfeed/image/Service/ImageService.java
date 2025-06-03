package org.example.newsfeed.image.Service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.image.dto.ImageUploadResponseDto;
import org.example.newsfeed.image.entity.Image;
import org.example.newsfeed.image.repository.ImageRepository;
import org.springframework.stereotype.Service;

/**
 * 이미지 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 현재는 이미지 저장 및 응답 DTO 반환 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    /**
     * 이미지 파일명을 기반으로 Image 엔티티를 생성 및 저장한 후,
     * 업로드 결과를 DTO로 반환합니다.
     *
     * @param fileName 저장할 이미지 파일 이름
     * @return ImageUploadResponseDto (이미지 ID 및 파일명 포함)
     */
    public ImageUploadResponseDto saveImage(String fileName) {
        // 이미지 엔티티 생성 및 저장
        Image image = new Image(fileName);
        imageRepository.save(image);

        // 저장된 이미지 정보를 DTO로 반환
        return new ImageUploadResponseDto(image.getId(), fileName);
    }
}
