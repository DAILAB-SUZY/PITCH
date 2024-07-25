package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentUpdateReq;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat/comment")
public class AlbumChatCommentApi {
    @Autowired
    private AlbumChatCommentService commentService;

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
    @Transactional
    public List<AlbumChatCommentResponse> getCommentByAlbumChatId(@RequestBody AlbumChatDto albumchat) {
        return commentService.getCommentsByAlbumChatId(albumchat.getAlbumChatId());
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
        )
    }
    )
    @PostMapping("create")
    @Transactional
    public AlbumChatCommentDto albumChatCommentCreate(@RequestBody AlbumChatCommentCreateReq comment) {
        return commentService.albumChatCommentCreate(comment);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),
        @ApiResponse(responseCode = "400",
            description = "Not Match AlbumChat" ,
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        ),
        @ApiResponse(responseCode = "404",
            description = "Not Found AlbumChatComment Or User",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )
    @PostMapping("update")
    @Transactional
    public ResponseEntity<?> albumChatCommentUpdate(@RequestBody AlbumChatCommentUpdateReq comment) {
        commentService.albumChatCommentUpdate(comment);
        return ResponseEntity.ok("성공");
    }

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
    @PostMapping("/delete")
    @Transactional
    public ResponseEntity<?> albumChatCommentDelete(@RequestBody AlbumChatCommentDto commentdto) {
        commentService.albumChatCommentDelete(commentdto);
        return ResponseEntity.ok("성공");
    }
}