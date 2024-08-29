package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.applications.ReplyService;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Reply.CreateReplyReq;
import org.cosmic.backend.domain.post.dtos.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dtos.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotFoundReplyException;
import org.cosmic.backend.domain.post.exceptions.NotMatchCommentException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 대댓글(Reply) 관련 API를 제공하는 REST 컨트롤러입니다.
 * 대댓글 조회, 생성, 수정, 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/reply")
@ApiCommonResponses
public class ReplyApi {
    private final ReplyService replyService;

    /**
     * ReplyApi의 생성자입니다.
     *
     * @param replyService 대댓글 관련 비즈니스 로직을 처리하는 서비스 클래스
     */
    public ReplyApi(ReplyService replyService) {
        this.replyService = replyService;
    }

    /**
     * 특정 댓글 ID로 대댓글 목록을 조회합니다.
     *
     * @param comment 조회할 댓글의 정보를 포함한 DTO 객체
     * @return 해당 댓글에 대한 대댓글 목록을 포함한 요청 객체 리스트
     *
     * @throws NotFoundCommentException 댓글을 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found Comment")
    public List<UpdateReplyReq> getRepliesByCommentId(@RequestBody CommentDto comment) {
        return replyService.getRepliesByCommentId(comment.getCommentId());
    }

    /**
     * 새로운 대댓글을 생성합니다.
     *
     * @param reply 생성할 대댓글의 요청 정보를 포함한 객체
     * @return 생성된 대댓글의 DTO 객체
     *
     * @throws NotFoundCommentException 댓글을 찾을 수 없을 때 발생합니다.
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found Comment or User")
    public ReplyDto createReply(@RequestBody CreateReplyReq reply) {
        return replyService.createReply(reply);
    }

    /**
     * 기존 대댓글을 수정합니다.
     *
     * @param reply 수정할 대댓글의 요청 정보를 포함한 객체
     * @return 수정 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundReplyException 대댓글을 찾을 수 없을 때 발생합니다.
     * @throws NotMatchCommentException 대댓글이 속한 댓글과 요청된 댓글이 일치하지 않을 때 발생합니다.
     * @throws NotMatchUserException 대댓글 작성자와 요청된 사용자가 일치하지 않을 때 발생합니다.
     */
    @PostMapping("/update")
    @ApiResponse(responseCode = "400", description = "Not Match User Or Comment")
    @ApiResponse(responseCode = "404", description = "Not Found Reply")
    public ResponseEntity<?> updateReply(@RequestBody UpdateReplyReq reply) {
        replyService.updateReply(reply);
        return ResponseEntity.ok("성공");
    }

    /**
     * 특정 대댓글을 삭제합니다.
     *
     * @param replydto 삭제할 대댓글의 정보를 포함한 DTO 객체
     * @return 삭제 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundReplyException 대댓글을 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found Reply")
    public ResponseEntity<?> deleteReply(@RequestBody ReplyDto replydto) {
        replyService.deleteReply(replydto.getReplyId());
        return ResponseEntity.ok("성공");
    }
}