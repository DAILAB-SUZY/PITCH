package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.post.applications.LikeService;
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 좋아요 관련 API를 제공하는 REST 컨트롤러입니다. 특정 게시글의 좋아요 조회, 좋아요 생성, 좋아요 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/album/post/{postId}/like")
@ApiCommonResponses
@Tag(name = "앨범 포스트 관련 API", description = "앨범 포스트 및 댓글/대댓글/좋아요")
@RequiredArgsConstructor
public class LikeApi {

  private final LikeService likeService;

  @PostMapping("")
  @ApiResponse(responseCode = "200", content = {
      @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = PostAndCommentsDetail.class))
  })
  @ApiResponse(responseCode = "404", description = "Not Found User or Post")
  @Operation(summary = "앨범 포스트 좋아요 API", description = "앨범 포스트에 대해 좋아요 혹은 좋아요를 취소합니다.")
  public ResponseEntity<PostAndCommentsDetail> likePost(@PathVariable Long postId,
      @AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(likeService.likeOrUnlikePost(postId, userId));
  }

}