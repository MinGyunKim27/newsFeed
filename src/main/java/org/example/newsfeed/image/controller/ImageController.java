package org.example.newsfeed.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.image.Service.ImageService;
import org.example.newsfeed.image.dto.ImageUploadResponseDto;
import org.example.newsfeed.image.exception.ImageUploadException;
import org.example.newsfeed.image.util.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 이미지 업로드 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    // 이미지 업로드 디렉토리 경로 (서버 내부 고정 경로)
    private final String uploadDir = "C:/upload/images/";

    private final ImageService imageService;

    /**
     * 이미지 업로드 API
     *
     * @param file 업로드할 이미지 파일 (multipart/form-data 형식)
     * @return 업로드 결과를 담은 DTO (파일명, URL 등)
     */
    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponseDto> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 1. 파일이 비어 있는지 확인
            if (file.isEmpty()) {
                throw new ImageUploadException("파일이 비어 있습니다.");
            }

            // 2. 이미지 파일인지 MIME 타입으로 확인 (ex. image/png, image/jpeg)
            if (!FileUtils.isImage(file.getContentType())) {
                throw new ImageUploadException("이미지 파일만 업로드할 수 있습니다.");
            }

            // 3. 업로드 경로 디렉토리 없을 경우 생성
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            // 4. 고유한 파일명 생성 및 경로 지정
            String fileName = FileUtils.generateUniqueFileName(file.getOriginalFilename());
            File dest = new File(uploadDir + fileName);

            // 5. 파일 저장
            file.transferTo(dest);

            // 6. DB 저장 및 응답 반환
            return new ResponseEntity<>(imageService.saveImage(fileName), HttpStatus.CREATED);

        } catch (IOException e) {
            // 파일 저장 도중 IOException 발생 시 사용자 정의 예외로 래핑
            throw new ImageUploadException("파일 저장 중 오류 발생", e);
        }
    }
}
