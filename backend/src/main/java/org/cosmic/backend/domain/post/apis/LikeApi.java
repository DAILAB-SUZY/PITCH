package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.applications.LikeService;
import org.cosmic.backend.domain.post.dtos.Like.LikeDto;
import org.cosmic.backend.domain.post.dtos.Like.LikeReq;
import org.cosmic.backend.domain.post.dtos.Like.LikeResponse;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
import org.cosmic.backend.domain.post.exceptions.ExistLikeException;
import org.cosmic.backend.domain.post.exceptions.NotFoundLikeException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 좋아요 관련 API를 제공하는 REST 컨트롤러입니다.
 * 특정 게시글의 좋아요 조회, 좋아요 생성, 좋아요 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/like")
@ApiCommonResponses
public class LikeApi {
    private final LikeService likeService;

    /**
     * LikeApi의 생성자입니다.
     *
     * @param likeService 좋아요 관련 비즈니스 로직을 처리하는 서비스 클래스
     */
    public LikeApi(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * 특정 게시글의 좋아요 목록을 조회합니다.
     *
     * @param post 조회할 게시글의 정보를 포함한 DTO 객체
     * @return 해당 게시글의 좋아요 응답 객체 리스트
     *
     * @throws NotFoundPostException 게시글을 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    public List<LikeResponse> getLikesByPostId(@RequestBody PostDto post) {
        return likeService.getLikesByPostId(post.getPostId());
    }

    /**
     * 새로운 좋아요를 생성합니다.
     *
     * @param like 생성할 좋아요의 정보를 포함한 DTO 객체
     * @return 생성된 좋아요 요청 객체
     *
     * @throws NotFoundPostException 게시글을 찾을 수 없을 때 발생합니다.
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     * @throws ExistLikeException 해당 사용자가 이미 게시글에 좋아요를 남겼을 때 발생합니다.
     */
    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found User or Post")
    @ApiResponse(responseCode = "409", description = "Like Already Exists",
            content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    public LikeReq createLike(@RequestBody LikeDto like) {
        return likeService.createLike(like.getUserId(), like.getPostId());
    }

    /**
     * 좋아요를 삭제합니다.
     *
     * @param likedto 삭제할 좋아요의 정보를 포함한 요청 객체
     * @return 삭제 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundLikeException 좋아요를 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found Like")
    public ResponseEntity<?> deleteLike(@RequestBody LikeReq likedto) {
        likeService.deleteLike(likedto.getUser_id(), likedto.getPost_id());
        return ResponseEntity.ok("성공");
    }
}