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

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final String uploadDir = "C:/upload/images/";
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponseDto> upload(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new ImageUploadException("파일이 비어 있습니다.");
            }

            if (!FileUtils.isImage(file.getContentType())) {
                throw new ImageUploadException("이미지 파일만 업로드할 수 있습니다.");
            }

            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String fileName = FileUtils.generateUniqueFileName(file.getOriginalFilename());
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            return new ResponseEntity<>(imageService.saveImage(fileName), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new ImageUploadException("파일 저장 중 오류 발생", e);
        }
    }
}
