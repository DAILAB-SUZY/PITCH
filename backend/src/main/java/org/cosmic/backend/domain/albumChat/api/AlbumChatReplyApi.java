package org.cosmic.backend.domain.albumChat.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.albumChat.dto.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dto.reply.AlbumChatReplyDto;
import org.cosmic.backend.domain.albumChat.dto.reply.CreateAlbumChatReplyReq;
import org.cosmic.backend.domain.albumChat.dto.reply.UpdateAlbumChatReplyReq;
import org.cosmic.backend.domain.albumChat.service.AlbumChatReplyService;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat/reply")
public class AlbumChatReplyApi {
    @Autowired
    private AlbumChatReplyService replyService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Not Found Comment",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/give")
    public List<UpdateAlbumChatReplyReq> getAlbumChatRepliesByCommentId(@RequestBody AlbumChatCommentDto comment) {
        return replyService.getAlbumChatRepliesByCommentId(comment.getAlbumChatCommentId());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Not Found Comment or User",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/create")
    public AlbumChatReplyDto createAlbumChatReply(@RequestBody CreateAlbumChatReplyReq reply) {
        return replyService.createAlbumChatReply(reply);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Not Match Comment Or User",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "Not Found Reply",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/update")
    public ResponseEntity<?> updateAlbumChatReply(@RequestBody UpdateAlbumChatReplyReq reply) {
        replyService.updateAlbumChatReply(reply);
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
                    description = "Not Found Reply",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/delete")
    public ResponseEntity<?> deleteAlbumChatReply(@RequestBody AlbumChatReplyDto replydto) {
        replyService.deleteAlbumChatReply(replydto.getAlbumChatReplyId());
        return ResponseEntity.ok("성공");
    }
}