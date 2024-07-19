package org.cosmic.backend.domain.albumChat.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.albumChat.dto.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dto.commentlike.AlbumChatCommentLikeDto;
import org.cosmic.backend.domain.albumChat.dto.commentlike.AlbumChatCommentLikeReq;
import org.cosmic.backend.domain.albumChat.dto.commentlike.AlbumChatCommentLikeResponse;
import org.cosmic.backend.domain.albumChat.service.AlbumChatCommentLikeService;
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
    public List<AlbumChatCommentLikeResponse> getAlbumChatCommentLikeByAlbumChatComment(@RequestBody AlbumChatCommentDto albumChatComment) {//특정 post의 좋아요만 보게해주는
        return likeService.getAlbumChatCommentLikeByAlbumChatComment(albumChatComment.getAlbumChatCommentId());
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
    public AlbumChatCommentLikeReq albumChatCreateCommentLike(@RequestBody AlbumChatCommentLikeDto like) {
        return likeService.albumChatCreateCommentLike(like.getUserId(),like.getAlbumChatCommentId());//유저 정보줌으로써 추가
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
    public ResponseEntity<?> deleteAlbumChatCommentLike(@RequestBody AlbumChatCommentLikeReq likedto) {
        likeService.deleteAlbumChatCommentLike(likedto.getAlbumChatCommentLikeId());
        return ResponseEntity.ok("성공");
    }
}

