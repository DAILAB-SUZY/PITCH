package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.post.dtos.Like.LikeDto;
import org.cosmic.backend.domain.post.dtos.Like.LikeReq;
import org.cosmic.backend.domain.post.dtos.Like.LikeResponse;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
import org.cosmic.backend.domain.post.services.LikeService;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/like")
public class LikeApi {
    //특정 post의 좋아요들임.
    @Autowired
    private LikeService likeService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Post",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/give")
    public List<LikeResponse> getLikesByPostId(@RequestBody PostDto post) {//특정 post의 좋아요만 보게해주는
        return likeService.getLikesByPostId(post.getPostId());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found User or Post",
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
    public LikeReq createLike(@RequestBody LikeDto like) {
        return likeService.createLike(like.getUserId(),like.getPostId());//유저 정보줌으로써 추가
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
    public ResponseEntity<?> deleteLike(@RequestBody LikeReq likedto) {
        likeService.deleteLike(likedto.getLikeId());//해당 likeid를 제거
        return ResponseEntity.ok("성공");
    }
}