package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.applications.PostService;
import org.cosmic.backend.domain.post.dtos.Post.CreatePost;
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostDetail;
import org.cosmic.backend.domain.post.dtos.Post.UpdatePost;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.search.applications.SearchAlbumService;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게시물(Post) 관련 API를 제공하는 REST 컨트롤러입니다. 게시물 조회, 생성, 수정, 삭제 및 앨범/아티스트 검색 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/album")
@ApiCommonResponses
@Tag(name = "앨범 포스트 관련 API", description = "앨범 포스트 및 댓글/대댓글/좋아요")
public class PostApi {

  private final PostService postService;
  private final SearchAlbumService searchAlbumService;

  /**
   * PostApi의 생성자입니다.
   *
   * @param postService 게시물 관련 비즈니스 로직을 처리하는 서비스 클래스
   */
  public PostApi(PostService postService,
      @Qualifier("searchAlbumService") SearchAlbumService searchAlbumService) {
    this.postService = postService;
    this.searchAlbumService = searchAlbumService;
  }

  /**
   * 앨범 포스트 조회 API
   *
   * @return 사용자의 모든 게시물을 포함한 요청 객체 리스트
   * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
   */
  @GetMapping("/post")
  @ApiResponse(responseCode = "404", description = "Not Found User")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = PostAndCommentsDetail.class))))
  @Operation(summary = "앨범 포스트 조회 API", description = "page번호와 limt개수를 이용해 앨범 포스트를 조회합니다.")
  public ResponseEntity<List<PostAndCommentsDetail>> giveAllPosts(
      @Parameter(description = "유저 id")
      @RequestParam(required = false) Long userId,
      @Parameter(description = "페이지 번호(0부터 시작)", required = true)
      @RequestParam Integer page,
      @Parameter(description = "페이지 당 포스트 수", required = true)
      @RequestParam Integer limit
  ) {
    return ResponseEntity.ok(postService.getPosts(userId, page, limit));
  }

  @GetMapping("/{spotifyAlbumId}/post")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = PostAndCommentsDetail.class))))
  @Operation(summary = "앨범에 대한 포스트 조회 API", description = "spotifyAlbumId에 대응하는 Album에 대한 albumpost들을 모두 가져옵니다.")
  public ResponseEntity<List<PostAndCommentsDetail>> getAlbumPostsBySpotifyAlbumId(
      @PathVariable String spotifyAlbumId,
      @Parameter(description = "페이지 번호(0부터 시작)", required = true)
      @RequestParam Integer page,
      @Parameter(description = "페이지 당 포스트 수", required = true)
      @RequestParam Integer limit) {
    return ResponseEntity.ok(postService.getPostsBySpotifyAlbumId(spotifyAlbumId, page, limit));
  }

  /**
   * 특정 게시물 ID로 게시물을 조회합니다.
   *
   * @param postId 조회할 게시물의 정보
   * @return 조회된 게시물의 요청 객체
   * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
   */
  @GetMapping("/post/{postId}")
  @ApiResponse(responseCode = "200", content = @Content(
      mediaType = MediaType.APPLICATION_JSON_VALUE,
      schema = @Schema(implementation = PostAndCommentsDetail.class)
  ))
  @ApiResponse(responseCode = "404", description = "Not Found Post")
  @Operation(summary = "특정 앨범 포스트 조회 API", description = "특정 앨범 포스트를 조회합니다.")
  public ResponseEntity<PostAndCommentsDetail> getPostById(@PathVariable Long postId) {
    return ResponseEntity.ok(postService.getPostById(postId));
  }

  /**
   * 새로운 게시물을 생성합니다.
   *
   * @param post 생성할 게시물의 정보를 포함한 객체
   * @return 생성된 게시물의 DTO 객체
   * @throws NotFoundAlbumException 앨범을 찾을 수 없을 때 발생합니다.
   */
  @PostMapping("/post")
  @ApiResponse(responseCode = "200", content = @Content(
      mediaType = MediaType.APPLICATION_JSON_VALUE,
      schema = @Schema(implementation = PostAndCommentsDetail.class)
  ))
  @ApiResponse(responseCode = "404", description = "Not Found Album")
  @Operation(summary = "특정 앨범 포스트 생성 API", description = "특정 앨범 포스트를 생성합니다.")
  public ResponseEntity<PostAndCommentsDetail> createPost(@RequestBody CreatePost post,
      @AuthenticationPrincipal Long userId) {
    try {
      return ResponseEntity.ok(
          postService.createPost(post.getContent(), post.getSpotifyAlbumId(),post.getScore(), userId));
    } catch (NotFoundAlbumException e) {
      searchAlbumService.saveArtistAndAlbumBySpotifyId(post.getSpotifyAlbumId());
    }
    return ResponseEntity.ok(
        postService.createPost(post.getContent(), post.getSpotifyAlbumId(),post.getScore(), userId));
  }

  /**
   * 기존 게시물을 수정합니다.
   *
   * @param post 수정할 게시물의 정보를 포함한 객체
   * @return 수정 성공 메시지를 포함한 {@link ResponseEntity}
   * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
   */
  @PostMapping("/post/{postId}")
  @ApiResponse(responseCode = "200", content = @Content(
      mediaType = MediaType.APPLICATION_JSON_VALUE,
      schema = @Schema(implementation = PostAndCommentsDetail.class)
  ))
  @ApiResponse(responseCode = "404", description = "Not Found Post")
  @Operation(summary = "특정 앨범 포스트 수정 API", description = "특정 앨범 포스트를 수정합니다.")
  public ResponseEntity<PostAndCommentsDetail> updatePost(@RequestBody UpdatePost post,
      @PathVariable Long postId, @AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(postService.updatePost(post.getContent(),post.getScore(), postId, userId));
  }

  /**
   * 특정 게시물을 삭제합니다.
   *
   * @return 삭제 성공 메시지를 포함한 {@link ResponseEntity}
   * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
   */
  @DeleteMapping("/post/{postId}")
  @ApiResponse(responseCode = "404", description = "Not Found Post")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      schema = @Schema(implementation = PostDetail.class)))
  @Operation(summary = "특정 앨범 포스트 삭제 API", description = "특정 앨범 포스트를 삭제합니다.")
  public ResponseEntity<String> deletePost(@PathVariable Long postId,
      @AuthenticationPrincipal Long userId) {
    postService.deletePost(postId, userId);
    return ResponseEntity.ok("ok");
  }

}
