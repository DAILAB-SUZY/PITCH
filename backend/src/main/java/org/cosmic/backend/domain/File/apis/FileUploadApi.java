package org.cosmic.backend.domain.File.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.File.applications.FileUploadService;
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

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Tag(name = "파일 관련 API", description = "파일 저장 및 불러오기")
public class FileUploadApi {
    @Value("${file.upload-dir}")
    private String uploadDirectory;
    private final FileUploadService fileUploadService;

    @PostMapping(value = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.MULTIPART_FORM_DATA_VALUE
            ,implementation= String.class))})
    @ApiResponse(responseCode = "500", description = "Failed to Upload Image")
    @Operation(summary = "파일 저장",description = "프로필 사진 변경")
    public ResponseEntity<String> addFile(
        @Param(value = "multipart/form-data 형식의 이미지")
        @Parameter(description = "파일 데이터")
        @RequestPart(required = false) MultipartFile profileImage,
        @AuthenticationPrincipal Long userId
        ) {
        fileUploadService.uploadFile(userId,profileImage);
        return ResponseEntity.ok("Upload successful");
    }

    @GetMapping("/{fileName}")
    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
            ,implementation= Resource.class))})
    @ApiResponse(responseCode = "404", description = "Not Found File")
    @ApiResponse(responseCode = "500", description = "Failed to Read File")
    @Operation(summary = "파일 불러오기",description = "파일이름으로 사진 가져오기")
    public ResponseEntity<Resource> getImage(
        @Parameter(description = "파일이름")
        @PathVariable String fileName) throws IOException {
        Resource resource=fileUploadService.getImageFile(fileName);
        String path = uploadDirectory+resource.getURI();
        String contentType = "application/octet-stream";
        if (resource.exists() && resource.isReadable()) {
            contentType = "image/webp";
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path + "\"")
            .body(resource);
    }
}