package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumLikeService;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDto;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeReq;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeResponse;
import org.cosmic.backend.domain.albumChat.exceptions.ExistAlbumLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundLikeException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * AlbumLikeApi 클래스는 앨범 챗에 대한 좋아요 기능을 처리하는 API를 제공합니다.
 */
@RestController
@RequestMapping("/api/albumchat/albumlike")
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
     * @param albumChat 조회할 앨범 챗의 ID를 포함한 AlbumChatDto 객체
     * @return List<AlbumChatAlbumLikeResponse> 조회된 좋아요 목록
     * @throws NotFoundAlbumChatException 특정 앨범 챗을 찾을 수 없는 경우 발생
     */
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChat")
    public List<AlbumChatAlbumLikeResponse> searchAlbumChatAlbumLikeByAlbumChatId(@RequestBody AlbumChatDto albumChat){
        return likeService.getAlbumChatAlbumLikeByAlbumChatId(albumChat.getAlbumChatId());
    }

    /**
     * 앨범 챗에 새로운 좋아요를 생성합니다.
     *
     * @param like 생성할 좋아요에 대한 정보가 담긴 AlbumChatAlbumLikeDto 객체
     * @return AlbumChatAlbumLikeReq 생성된 좋아요의 ID를 포함한 객체
     * @throws NotFoundUserException 특정 사용자를 찾을 수 없는 경우 발생
     * @throws NotFoundAlbumChatException 특정 앨범 챗을 찾을 수 없는 경우 발생
     * @throws ExistAlbumLikeException 이미 좋아요가 존재하는 경우 발생
     */
    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found User or AlbumChat")
    @ApiResponse(responseCode = "409", description = "Like Already Exists")
    public AlbumChatAlbumLikeReq albumChatAlbumLikeCreate(@RequestBody AlbumChatAlbumLikeDto like) {
        return likeService.albumChatAlbumLikeCreate(like.getUserId(),like.getAlbumChatId());
    }

    /**
     * 주어진 좋아요 ID에 해당하는 좋아요를 삭제합니다.
     *
     * @param likedto 삭제할 좋아요의 ID를 포함한 AlbumChatAlbumLikeReq 객체
     * @return ResponseEntity<?> 삭제 성공 시 성공 메시지 반환
     * @throws NotFoundLikeException 특정 좋아요를 찾을 수 없는 경우 발생
     */
    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found Like")
    public ResponseEntity<?> albumChatAlbumLikeDelete(@RequestBody AlbumChatAlbumLikeReq likedto) {
        likeService.albumChatAlbumLikeDelete(likedto.getAlbumChatAlbumLikeId());
        return ResponseEntity.ok("성공");
    }
}