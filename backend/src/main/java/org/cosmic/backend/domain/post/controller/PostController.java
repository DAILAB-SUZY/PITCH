package org.cosmic.backend.domain.post.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.dto.ArtistDTO;
import org.cosmic.backend.domain.post.dto.Post.*;
import org.cosmic.backend.domain.post.service.PostService;
import org.cosmic.backend.domain.user.dto.userDto;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "404",
            description = "Not Found User",
            content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )
    @Transactional
    @PostMapping("/give")
    public List<PostReq> giveAllPosts(@RequestBody userDto user) {
        return postService.getAllPosts(user.getUserid());
    }

    //모든 포스트 줄 때

    @PostMapping("/open")
    public Post getPostById(@RequestBody PostDto post) {//Post가 아니라 post에서 나와야할 내용들이 리턴되야함
        return postService.getPostById(post.getUserId(),post.getPostId());
    }
    //특정 post열 때

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post) {//Post로 내용이 추가됨
        postService.createPost(post);
        return ResponseEntity.ok("성공");
    }
    //포스트 하나 만들 때

    @PostMapping("/update")
    public ResponseEntity<?> updatePost(@RequestBody Post post) {//Post로 내용이 추가됨
        postService.updatePost(post);
        return ResponseEntity.ok("성공");
    }
    //업데이트할 때

    @PostMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestBody PostDto post) {//postid가져오면
        postService.deletePost(post.getUserId(),post.getPostId());
        return ResponseEntity.ok("성공");
    }
    //포스트 하나 없앨때

    @PostMapping("/searchAlbum")
    public List<AlbumDto> searchAlbum(@RequestBody Album album) {//postid가져오면
        return postService.searchAlbum(album);
    }
    //앨범이름으로 찾기

    @PostMapping("/searchArtist")
    public List<AlbumDto> searchArtist(@RequestBody Artist artist) {//postid가져오면
        return postService.searchArtist(artist);
    }
    //아티스트이름으로 찾기
}
