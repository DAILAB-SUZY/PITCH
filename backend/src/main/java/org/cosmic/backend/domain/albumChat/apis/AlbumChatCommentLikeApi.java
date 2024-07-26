package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDto;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeIdResponse;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat/commentlike")
public class AlbumChatCommentLikeApi {//댓글 마다의 좋아요

    @Autowired
    private AlbumChatCommentLikeService likeService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "404",
            description = "Not Found AlbumChatComment",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )
    @PostMapping("/give")
    public List<AlbumChatCommentLikeResponse> albumChatCommentLikeGetByAlbumChatCommentId
        (@RequestBody AlbumChatCommentDto albumChatComment) {
        return likeService.getAlbumChatCommentLikeByAlbumChatCommentId(albumChatComment.getAlbumChatCommentId());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "404",
            description = "Not Found User or AlbumChatComment",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        ),
        @ApiResponse(responseCode = "409",
            description = "CommentLike Already Exists",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )
    @PostMapping("/create")
    public AlbumChatCommentLikeIdResponse albumChatCommentLikeCreate(@RequestBody AlbumChatCommentLikeDto like) {
        return likeService.albumChatCommentLikeCreate(like.getUserId(),like.getAlbumChatCommentId());
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
            description = "Not Found CommentLike",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    })

    @PostMapping("/delete")
    public ResponseEntity<?> albumChatCommentLikeDelete(@RequestBody AlbumChatCommentLikeIdResponse likedto) {
        likeService.albumChatCommentLikeDelete(likedto.getAlbumChatCommentLikeId());
        return ResponseEntity.ok("성공");
    }
}

