package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatReplyService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyDto;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyResponse;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyUpdateReq;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotFoundReplyException;
import org.cosmic.backend.domain.post.exceptions.NotMatchCommentException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AlbumChatReplyApi는 앨범 챗 댓글의 답글과 관련된 API를 제공합니다.
 */
@RestController
@RequestMapping("/api/albumchat/reply")
public class AlbumChatReplyApi {
    private final AlbumChatReplyService replyService;

    /**
     * AlbumChatReplyApi 생성자.
     *
     * @param replyService 앨범 챗 댓글 답글 서비스 객체
     */
    public AlbumChatReplyApi(AlbumChatReplyService replyService) {
        this.replyService = replyService;
    }

    /**
     * 특정 댓글에 대한 답글 목록을 반환합니다.
     *
     * @param comment 댓글 정보 DTO
     * @return List<AlbumChatReplyResponse> 답글 목록
     * @throws NotFoundCommentException 댓글이 존재하지 않을 경우 발생
     */
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found Comment")
    public List<AlbumChatReplyResponse> albumChatRepliesGetByCommentId(@RequestBody AlbumChatCommentDto comment) {
        return replyService.albumChatRepliesGetByCommentId(comment.getAlbumChatCommentId());
    }

    /**
     * 새로운 답글을 생성합니다.
     *
     * @param reply 답글 생성 요청 DTO
     * @return AlbumChatReplyDto 생성된 답글 정보
     * @throws NotFoundCommentException 댓글이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     */
    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found Comment or User")
    public AlbumChatReplyDto albumChatReplyCreate(@RequestBody AlbumChatReplyCreateReq reply) {
        return replyService.albumChatReplyCreate(reply);
    }

    /**
     * 기존 답글을 업데이트합니다.
     *
     * @param reply 답글 업데이트 요청 DTO
     * @return ResponseEntity 성공 메시지
     * @throws NotFoundReplyException 답글이 존재하지 않을 경우 발생
     * @throws NotMatchCommentException 답글이 해당 댓글에 속하지 않는 경우 발생
     * @throws NotMatchUserException 사용자가 일치하지 않는 경우 발생
     */
    @PostMapping("/update")
    @ApiResponse(responseCode = "400", description = "Not Match Comment Or User")
    @ApiResponse(responseCode = "404", description = "Not Found Reply")
    public ResponseEntity<?> albumChatReplyUpdate(@RequestBody AlbumChatReplyUpdateReq reply) {
        replyService.albumChatReplyUpdate(reply);
        return ResponseEntity.ok("성공");
    }

    /**
     * 특정 답글을 삭제합니다.
     *
     * @param replydto 삭제할 답글 정보 DTO
     * @return ResponseEntity 성공 메시지
     * @throws NotFoundReplyException 답글이 존재하지 않을 경우 발생
     */
    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found Reply")
    public ResponseEntity<?> albumChatReplyDelete(@RequestBody AlbumChatReplyDto replydto) {
        replyService.albumChatReplyDelete(replydto.getAlbumChatReplyId());
        return ResponseEntity.ok("성공");
    }
}