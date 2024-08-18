package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatService;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
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
    private final AlbumChatService albumChatService;

    public AlbumChatApi(AlbumChatService albumChatService) {
        this.albumChatService = albumChatService;
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
    @PostMapping("/open")
    public AlbumChatResponse getAlbumChatById(@RequestBody AlbumDto album) {
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
    public List<AlbumChatCommentResponse> getAlbumChatCommentByManyLikeId(@RequestBody AlbumChatDto albumchat) {
        return albumChatService.getAlbumChatCommentByManyLikeId(albumchat);
    }
}