package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatService;
import org.cosmic.backend.domain.post.dto.Post.AlbumDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat")
public class AlbumChatApi {
    @Autowired
    private AlbumChatService albumChatService;

    //모든 포스트 줄 때
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
    @Transactional
    @PostMapping("/open")
    public AlbumChatResponse getAlbumChatById(@RequestBody AlbumDto album) {//Post가 아니라 post에서 나와야할 내용들이 리턴되야함
        return albumChatService.getAlbumChatById(album);
    }

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

    @Transactional
    @PostMapping("/manylike")
    public List<AlbumChatCommentResponse> getAlbumChatCommentByManyLikeId(@RequestBody AlbumChatDto albumchat) {//Post가 아니라 post에서 나와야할 내용들이 리턴되야함
        return albumChatService.getAlbumChatCommentByManyLikeId(albumchat);
    }
    //좋아요 많이 받은 순으로 댓글 나열
}