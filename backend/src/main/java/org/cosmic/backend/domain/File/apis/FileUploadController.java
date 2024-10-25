package org.cosmic.backend.domain.File.apis;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.File.applications.FileUploadService;
import org.cosmic.backend.domain.File.exceptions.ImageSaveFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileUploadController {
    @Value("${file.upload-dir}")
    private String uploadDirectory;
    private final FileUploadService fileUploadService;

    //upload
    @PostMapping(value = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addFile(
        @Param(value = "multipart/form-data 형식의 이미지")
        @RequestPart(required = false) MultipartFile profileImage,
        @AuthenticationPrincipal Long userId
        ) throws ImageSaveFailedException, MalformedURLException {
         // 서버 내부 스토리지 저장
        fileUploadService.uploadFile(userId,profileImage);
        return ResponseEntity.ok("Upload successful");
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName)
            throws IOException {
        Resource resource=fileUploadService.getImageFile(fileName);
        String path = uploadDirectory+resource.getURI();
        String contentType = "application/octet-stream"; // 기본 MIME 타입
        if (resource.exists() && resource.isReadable()) {
            // 확장자 기반으로 MIME 타입 설정 가능
            contentType = "image/webp"; // 파일 형식에 맞게 변경하세요.
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path + "\"")
                .body(resource);
    }
}