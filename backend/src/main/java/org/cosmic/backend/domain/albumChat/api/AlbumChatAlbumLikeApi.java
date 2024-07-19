package org.cosmic.backend.domain.albumChat.api;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.albumChat.dto.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dto.albumlike.AlbumChatAlbumLikeDto;
import org.cosmic.backend.domain.albumChat.dto.albumlike.AlbumChatAlbumLikeReq;
import org.cosmic.backend.domain.albumChat.dto.albumlike.AlbumChatAlbumLikeResponse;
import org.cosmic.backend.domain.albumChat.service.AlbumChatAlbumLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat/albumlike")
public class AlbumChatAlbumLikeApi {//각 앨범의 총 좋아요
    @Autowired
    private AlbumChatAlbumLikeService likeService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found AlbumChat",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/give")
    public List<AlbumChatAlbumLikeResponse> getAlbumChatAlbumLikeByAlbumChatId(@RequestBody AlbumChatDto albumChat) {//특정 post의 좋아요만 보게해주는
        return likeService.getAlbumChatAlbumLikeByAlbumChatId(albumChat.getAlbumChatId());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found User or AlbumChat",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            ),
            @ApiResponse(responseCode = "409",
                    description = "Like Already Exists",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/create")
    public AlbumChatAlbumLikeReq albumChatCreateAlbumLike(@RequestBody AlbumChatAlbumLikeDto like) {
        return likeService.albumChatCreateAlbumLike(like.getUserId(),like.getAlbumChatId());//유저 정보줌으로써 추가
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "Not Found Like",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    })

    @PostMapping("/delete")
    public ResponseEntity<?> deleteAlbumChatAlbumLike(@RequestBody AlbumChatAlbumLikeReq likedto) {
        likeService.deleteAlbumChatAlbumLike(likedto.getAlbumChatAlbumLikeId());//해당 likeid를 제거
        return ResponseEntity.ok("성공");
    }
}