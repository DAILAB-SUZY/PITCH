package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentLikeService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDto;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeIdResponse;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeResponse;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat/commentlike")
@ApiCommonResponses
public class AlbumChatCommentLikeApi {//댓글 마다의 좋아요
    private final AlbumChatCommentLikeService likeService;

    public AlbumChatCommentLikeApi(AlbumChatCommentLikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
    public List<AlbumChatCommentLikeResponse> albumChatCommentLikeGetByAlbumChatCommentId
        (@RequestBody AlbumChatCommentDto albumChatComment) {
        return likeService.getAlbumChatCommentLikeByAlbumChatCommentId(albumChatComment.getAlbumChatCommentId());
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found User or AlbumChatComment")
    @ApiResponse(responseCode = "409", description = "CommentLike Already Exists")
    public AlbumChatCommentLikeIdResponse albumChatCommentLikeCreate(@RequestBody AlbumChatCommentLikeDto like) {
        return likeService.albumChatCommentLikeCreate(like.getUserId(),like.getAlbumChatCommentId());
    }

    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found CommentLike")
    public ResponseEntity<?> albumChatCommentLikeDelete(@RequestBody AlbumChatCommentLikeIdResponse likedto) {
        likeService.albumChatCommentLikeDelete(likedto.getAlbumChatCommentLikeId());
        return ResponseEntity.ok("성공");
    }
}

