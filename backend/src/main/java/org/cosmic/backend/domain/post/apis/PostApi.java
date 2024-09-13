package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.playList.dtos.ArtistDto;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.applications.PostService;
import org.cosmic.backend.domain.post.dtos.Post.*;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시물(Post) 관련 API를 제공하는 REST 컨트롤러입니다.
 * 게시물 조회, 생성, 수정, 삭제 및 앨범/아티스트 검색 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/album/post")
@ApiCommonResponses
public class PostApi {
    private final PostService postService;

    /**
     * PostApi의 생성자입니다.
     *
     * @param postService 게시물 관련 비즈니스 로직을 처리하는 서비스 클래스
     */
    public PostApi(PostService postService) {
        this.postService = postService;
    }

    /**
     * 특정 사용자의 모든 게시물을 조회합니다.
     *
     * @return 사용자의 모든 게시물을 포함한 요청 객체 리스트
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     */
    @GetMapping("/")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<PostReq> giveAllPosts(@RequestParam Long userId) {
        return postService.getAllPosts(userId);
    }

    /**
     * 특정 게시물 ID로 게시물을 조회합니다.
     *
     * @param postId 조회할 게시물의 정보
     * @return 조회된 게시물의 요청 객체
     *
     * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
     */
    @GetMapping("/{postId}")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    public ResponseEntity<PostDetail> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    /**
     * 새로운 게시물을 생성합니다.
     *
     * @param post 생성할 게시물의 정보를 포함한 객체
     * @return 생성된 게시물의 DTO 객체
     *
     * @throws NotFoundAlbumException 앨범을 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/")
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    public PostDto createPost(@RequestBody CreatePost post, @AuthenticationPrincipal Long userId) {
        return postService.createPost(post.getContent(), post.getAlbumId(), userId);
    }

    /**
     * 기존 게시물을 수정합니다.
     *
     * @param post 수정할 게시물의 정보를 포함한 객체
     * @return 수정 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/{postId}")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    public ResponseEntity<?> updatePost(@RequestBody UpdatePost post, @PathVariable Long postId, @AuthenticationPrincipal Long userId) {
        postService.updatePost(post.getContent(), postId, userId);
        return ResponseEntity.ok("성공");
    }

    /**
     * 특정 게시물을 삭제합니다.
     *
     * @return 삭제 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
     */
    @DeleteMapping("/{postId}")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.ok("성공");
    }

    /**
     * 앨범 이름으로 앨범을 검색합니다.
     *
     * @param album 검색할 앨범의 정보를 포함한 DTO 객체
     * @return 해당 앨범의 정보를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundAlbumException 앨범을 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/searchAlbum")
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    public List<AlbumDto> searchAlbum(@RequestBody AlbumDto album) {
        return postService.searchAlbum(album.getAlbumName());
    }

    /**
     * 아티스트 이름으로 앨범을 검색합니다.
     *
     * @param artist 검색할 아티스트의 정보를 포함한 DTO 객체
     * @return 해당 아티스트의 앨범 정보를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundArtistException 아티스트를 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/searchArtist")
    @ApiResponse(responseCode = "404", description = "Not Found Artist")
    public List<AlbumDto> searchArtist(@RequestBody ArtistDto artist) {
        return postService.searchArtist(artist.getArtistName());
    }
}
