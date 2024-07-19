package org.cosmic.backend.domain.albumChat.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.albumChat.dto.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dto.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dto.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.dto.comment.CreateAlbumChatCommentReq;
import org.cosmic.backend.domain.albumChat.dto.comment.UpdateAlbumChatCommentReq;
import org.cosmic.backend.domain.albumChat.service.AlbumChatCommentService;
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
    public List<AlbumChatCommentResponse> getCommentsByAlbumChat(@RequestBody AlbumChatDto albumchat) {//postid에 있는 comment들을
        return commentService.getCommentsByAlbumChat(albumchat.getAlbumChatId());
    }
    //특정 앨범에 대한 comment들을 모두 가져옴

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
    public AlbumChatCommentDto createAlbumChatComment(@RequestBody CreateAlbumChatCommentReq comment) {
        return commentService.createAlbumChatComment(comment);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Not Found AlbumChatComment",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "Not Found AlbumChatComment",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("update")
    @Transactional
    public ResponseEntity<?> updateAlbumChatComment(@RequestBody UpdateAlbumChatCommentReq comment) {
        commentService.updateAlbumChatComment(comment);
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
    public ResponseEntity<?> deleteAlbumChatComment(@RequestBody AlbumChatCommentDto commentdto) {
        commentService.deleteAlbumChatComment(commentdto);
        return ResponseEntity.ok("성공");
    }
}