package org.cosmic.backend.domain.post.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cosmic.backend.domain.post.dto.Comment.CommentDto;
import org.cosmic.backend.domain.post.dto.Reply.CreateReplyReq;
import org.cosmic.backend.domain.post.dto.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dto.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.post.service.ReplyService;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reply")
public class ReplyApi {
    @Autowired
    private ReplyService replyService;

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
    public List<UpdateReplyReq> getRepliesByCommentId(@RequestBody CommentDto comment) {
        return replyService.getRepliesByCommentId(comment.getCommentId());
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
    public ReplyDto createReply(@RequestBody CreateReplyReq reply) {
        return replyService.createReply(reply);
    }
    //comment를 부모로하는 reply를 쭉 받고 시간 순으로 배치

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Not Match User Or Comment",
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
    @PostMapping("/update")
    public ResponseEntity<?> updateReply(@RequestBody UpdateReplyReq reply) {
        replyService.updateReply(reply);
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
    public ResponseEntity<?> deleteReply(@RequestBody ReplyDto replydto) {
        replyService.deleteReply(replydto.getReplyId());
        return ResponseEntity.ok("성공");
    }
}