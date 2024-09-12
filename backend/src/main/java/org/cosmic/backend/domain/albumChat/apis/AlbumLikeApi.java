package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumLikeService;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDetail;
import org.cosmic.backend.domain.albumChat.exceptions.ExistAlbumLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundLikeException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
@ApiCommonResponses
public class AlbumLikeApi {//각 앨범의 총 좋아요
    private final AlbumLikeService likeService;
    /**
     * AlbumLikeApi 생성자.
     *
     * @param likeService AlbumLikeService 주입
     */
    public AlbumLikeApi(AlbumLikeService likeService) {
        this.likeService = likeService;
    }
    /**
     * 앨범 챗 ID를 사용하여 해당 앨범 챗의 좋아요 목록을 조회합니다.
     *
     * @param albumId 조회할 앨범 ID
     * @return List<AlbumChatAlbumLikeDetail> 조회된 좋아요 목록
     * @throws NotFoundAlbumChatException 특정 앨범 챗을 찾을 수 없는 경우 발생
     */
    @GetMapping("/album/{albumId}/albumLike")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChat")
    public ResponseEntity<List<AlbumChatAlbumLikeDetail>> searchAlbumChatAlbumLikeByAlbumId(@PathVariable Long albumId){
        return ResponseEntity.ok(likeService.getAlbumChatAlbumLikeByAlbumChatId(albumId));
    }

    /**
     * 앨범 챗에 새로운 좋아요를 생성합니다.
     *
     *
     * @return AlbumLikeReq 생성된 좋아요의 ID를 포함한 객체
     * @throws NotFoundUserException 특정 사용자를 찾을 수 없는 경우 발생
     * @throws NotFoundAlbumChatException 특정 앨범 챗을 찾을 수 없는 경우 발생
     * @throws ExistAlbumLikeException 이미 좋아요가 존재하는 경우 발생
     */
    @PostMapping("/album/{albumId}/albumLike")
    @ApiResponse(responseCode = "404", description = "Not Found User or AlbumChat")
    @ApiResponse(responseCode = "409", description = "Like Already Exists")
    public ResponseEntity<List<AlbumChatAlbumLikeDetail>> albumChatAlbumLikeCreate(@PathVariable Long albumId,@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(likeService.albumChatAlbumLikeCreate(userId,albumId));
    }

    /**
     * 주어진 좋아요 ID에 해당하는 좋아요를 삭제합니다.
     *
     * @param userId 삭제할 좋아요의 ID를 포함한 AlbumLikeReq 객체
     * @return ResponseEntity<?> 삭제 성공 시 성공 메시지 반환
     * @throws NotFoundLikeException 특정 좋아요를 찾을 수 없는 경우 발생
     */
    @DeleteMapping("/album/{albumId}/albumLike")
    @ApiResponse(responseCode = "404", description = "Not Found Like")
    public ResponseEntity<List<AlbumChatAlbumLikeDetail>> albumChatAlbumLikeDelete(@PathVariable Long albumId,@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(likeService.albumChatAlbumLikeDelete(albumId,userId));
    }
}