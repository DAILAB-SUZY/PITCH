package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatAlbumLikeService;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDto;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeReq;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeResponse;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat/albumlike")
@ApiCommonResponses
public class AlbumChatAlbumLikeApi {//각 앨범의 총 좋아요
    private final AlbumChatAlbumLikeService likeService;

    public AlbumChatAlbumLikeApi(AlbumChatAlbumLikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChat")
    public List<AlbumChatAlbumLikeResponse> searchAlbumChatAlbumLikeByAlbumChatId(@RequestBody AlbumChatDto albumChat){
        return likeService.getAlbumChatAlbumLikeByAlbumChatId(albumChat.getAlbumChatId());
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found User or AlbumChat")
    @ApiResponse(responseCode = "409", description = "Like Already Exists")
    public AlbumChatAlbumLikeReq albumChatAlbumLikeCreate(@RequestBody AlbumChatAlbumLikeDto like) {
        return likeService.albumChatAlbumLikeCreate(like.getUserId(),like.getAlbumChatId());
    }

    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found Like")
    public ResponseEntity<?> albumChatAlbumLikeDelete(@RequestBody AlbumChatAlbumLikeReq likedto) {
        likeService.albumChatAlbumLikeDelete(likedto.getAlbumChatAlbumLikeId());
        return ResponseEntity.ok("성공");
    }
}