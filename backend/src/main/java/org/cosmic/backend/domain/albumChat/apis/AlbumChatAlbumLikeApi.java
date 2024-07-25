package org.cosmic.backend.domain.albumChat.apis;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDto;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeReq;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatAlbumLikeService;
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
    public List<AlbumChatAlbumLikeResponse> searchAlbumChatAlbumLikeByAlbumChatId(@RequestBody AlbumChatDto albumChat){
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
    public AlbumChatAlbumLikeReq albumChatAlbumLikeCreate(@RequestBody AlbumChatAlbumLikeDto like) {
        return likeService.albumChatAlbumLikeCreate(like.getUserId(),like.getAlbumChatId());
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
    public ResponseEntity<?> albumChatAlbumLikeDelete(@RequestBody AlbumChatAlbumLikeReq likedto) {
        likeService.albumChatAlbumLikeDelete(likedto.getAlbumChatAlbumLikeId());
        return ResponseEntity.ok("성공");
    }
}