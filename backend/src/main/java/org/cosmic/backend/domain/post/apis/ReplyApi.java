package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Reply.CreateReplyReq;
import org.cosmic.backend.domain.post.dtos.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dtos.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.post.services.ReplyService;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reply")
@ApiCommonResponses
public class ReplyApi {
    private final ReplyService replyService;

    public ReplyApi(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found Comment")
    public List<UpdateReplyReq> getRepliesByCommentId(@RequestBody CommentDto comment) {
        return replyService.getRepliesByCommentId(comment.getCommentId());
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found Comment or User")
    public ReplyDto createReply(@RequestBody CreateReplyReq reply) {
        return replyService.createReply(reply);
    }
    //comment를 부모로하는 reply를 쭉 받고 시간 순으로 배치
    @PostMapping("/update")
    @ApiResponse(responseCode = "400", description = "Not Match User Or Comment")
    @ApiResponse(responseCode = "404", description = "Not Found Reply")
    public ResponseEntity<?> updateReply(@RequestBody UpdateReplyReq reply) {
        replyService.updateReply(reply);
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found Reply")
    public ResponseEntity<?> deleteReply(@RequestBody ReplyDto replydto) {
        replyService.deleteReply(replydto.getReplyId());
        return ResponseEntity.ok("성공");
    }
}