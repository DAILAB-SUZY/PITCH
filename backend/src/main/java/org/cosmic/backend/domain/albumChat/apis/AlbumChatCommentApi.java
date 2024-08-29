package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentService;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentUpdateReq;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.exceptions.NotMatchAlbumChatException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AlbumChatCommentApi 클래스는 앨범 챗의 댓글 관리를 위한 API를 제공합니다.
 * 댓글 조회, 생성, 수정, 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/albumchat/comment")
@ApiCommonResponses
public class AlbumChatCommentApi {
    private final AlbumChatCommentService commentService;

    /**
     * AlbumChatCommentApi 생성자.
     *
     * @param commentService 댓글 서비스 주입
     */
    public AlbumChatCommentApi(AlbumChatCommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 특정 앨범 챗 ID로 댓글 목록을 조회하는 API.
     *
     * @param albumchat 조회할 앨범 챗 ID를 포함한 DTO
     * @return List<AlbumChatCommentResponse> 조회된 댓글 목록
     * @throws NotFoundAlbumChatException 앨범 챗이 존재하지 않을 경우 발생
     */
    @PostMapping("/give")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChat")
    public List<AlbumChatCommentResponse> getCommentByAlbumChatId(@RequestBody AlbumChatDto albumchat) {
        return commentService.getCommentsByAlbumChatId(albumchat.getAlbumChatId());
    }

    /**
     * 새로운 앨범 챗 댓글을 생성하는 API
     *
     * @param comment 생성할 댓글 정보가 담긴 DTO
     * @return AlbumChatCommentDto 생성된 댓글 정보
     * @throws NotFoundAlbumChatException 앨범 챗이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     */
    @PostMapping("/create")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User or AlbumChat")
    public AlbumChatCommentDto albumChatCommentCreate(@RequestBody AlbumChatCommentCreateReq comment) {
        return commentService.albumChatCommentCreate(comment);
    }

    /**
     * 기존의 앨범 챗 댓글을 수정하는 API
     *
     * @param comment 수정할 댓글 정보가 담긴 DTO
     * @return ResponseEntity<?> 성공 메시지 반환
     * @throws NotMatchAlbumChatException 앨범 챗 ID가 일치하지 않을 경우 발생
     * @throws NotMatchUserException 수정하려는 사용자와 기존 댓글 생성한 사용자가 다를때 발생
     * @throws NotFoundAlbumChatCommentException 댓글이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     */
    @PostMapping("/update")
    @Transactional
    @ApiResponse(responseCode = "400", description = "Not Match AlbumChat or User")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment Or User")
    public ResponseEntity<?> albumChatCommentUpdate(@RequestBody AlbumChatCommentUpdateReq comment) {
        commentService.albumChatCommentUpdate(comment);
        return ResponseEntity.ok("성공");
    }

    /**
     * 앨범 챗 댓글을 삭제하는 API
     *
     * @param commentdto 삭제할 댓글 정보가 담긴 DTO
     * @return ResponseEntity<?> 성공 메시지 반환
     * @throws NotFoundAlbumChatCommentException 댓글이 존재하지 않을 경우 발생
     */
    @PostMapping("/delete")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
    //TODO NOTMATCHUSER오류 추가
    public ResponseEntity<?> albumChatCommentDelete(@RequestBody AlbumChatCommentDto commentdto) {
        commentService.albumChatCommentDelete(commentdto);
        return ResponseEntity.ok("성공");
    }
}