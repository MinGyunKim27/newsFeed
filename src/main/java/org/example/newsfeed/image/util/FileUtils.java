package org.example.newsfeed.image.util;

public class FileUtils {

    public static String getExtension(String filename) {
        int lastDot = filename.lastIndexOf(".");
        if (lastDot == -1) return "";
        return filename.substring(lastDot + 1).toLowerCase();
    }
    // 이미지 파일 여부 확인
    public static boolean isImage(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    // 저장 경로 생성
    public static String generateUniqueFileName(String originalFilename) {
        String extension = getExtension(originalFilename);
        return java.util.UUID.randomUUID() + "_" + originalFilename;
    }
}
