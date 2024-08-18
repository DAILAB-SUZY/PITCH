package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatReplyService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyDto;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyResponse;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyUpdateReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat/reply")
public class AlbumChatReplyApi {
    private final AlbumChatReplyService replyService;

    public AlbumChatReplyApi(AlbumChatReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found Comment")
    public List<AlbumChatReplyResponse> albumChatRepliesGetByCommentId(@RequestBody AlbumChatCommentDto comment) {
        return replyService.albumChatRepliesGetByCommentId(comment.getAlbumChatCommentId());
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found Comment or User")
    public AlbumChatReplyDto albumChatReplyCreate(@RequestBody AlbumChatReplyCreateReq reply) {
        return replyService.albumChatReplyCreate(reply);
    }

    @PostMapping("/update")
    @ApiResponse(responseCode = "400", description = "Not Match Comment Or User")
    @ApiResponse(responseCode = "404", description = "Not Found Reply")
    public ResponseEntity<?> albumChatReplyUpdate(@RequestBody AlbumChatReplyUpdateReq reply) {
        replyService.albumChatReplyUpdate(reply);
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found Reply")
    public ResponseEntity<?> albumChatReplyDelete(@RequestBody AlbumChatReplyDto replydto) {
        replyService.albumChatReplyDelete(replydto.getAlbumChatReplyId());
        return ResponseEntity.ok("성공");
    }
}