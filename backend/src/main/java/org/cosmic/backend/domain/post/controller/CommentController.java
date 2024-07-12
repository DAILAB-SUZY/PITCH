package org.cosmic.backend.domain.post.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.post.dto.Comment.CommentDto;
import org.cosmic.backend.domain.post.dto.Comment.CommentReq;
import org.cosmic.backend.domain.post.dto.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dto.Comment.UpdateCommentReq;
import org.cosmic.backend.domain.post.dto.Post.PostDto;
import org.cosmic.backend.domain.post.service.CommentService;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

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
    @Transactional
    public List<CommentReq> getCommentsByPostId(@RequestBody PostDto post) {//postid에 있는 comment들을
        return commentService.getCommentsByPostId(post.getPostId());
    }
    //어떤 user의 postid인지

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
            )
    }
    )
    @PostMapping("create")
    @Transactional
    public CommentDto createComment(@RequestBody CreateCommentReq comment) {
        return commentService.createComment(comment);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "401",
                    description = "Not Found Post",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("update")
    @Transactional
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentReq comment) {
        commentService.updateComment(comment);
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
                    description = "Not Found Comment",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteComment(@RequestBody CommentDto commentdto) {
        commentService.deleteComment(commentdto);
        return ResponseEntity.ok("성공");
    }
}
