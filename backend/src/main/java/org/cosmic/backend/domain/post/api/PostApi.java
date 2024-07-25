package org.cosmic.backend.domain.post.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.dto.ArtistDto;
import org.cosmic.backend.domain.post.dto.Post.*;
import org.cosmic.backend.domain.post.service.PostService;
import org.cosmic.backend.domain.user.dtos.UserDto;
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
public class PostApi {
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
    public List<PostReq> giveAllPosts(@RequestBody UserDto user) {
        return postService.getAllPosts(user.getUserId());
    }

    //모든 포스트 줄 때
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Post",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @Transactional
    @PostMapping("/open")
    public PostReq getPostById(@RequestBody PostDto post) {//Post가 아니라 post에서 나와야할 내용들이 리턴되야함
        return postService.getPostById(post.getPostId());
    }
    //특정 post열 때

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Album",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/create")
    public PostDto createPost(@RequestBody CreatePost post) {//Post로 내용이 추가됨
        return postService.createPost(post);
    }

    //포스트 하나 만들 때
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Post",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/update")
    public ResponseEntity<?> updatePost(@RequestBody UpdatePost post) {//Post로 내용이 추가됨
        postService.updatePost(post);
        return ResponseEntity.ok("성공");
    }
    //업데이트할 때
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Post",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestBody PostDto post) {//postid가져오면
        postService.deletePost(post.getPostId());
        return ResponseEntity.ok("성공");
    }
    //포스트 하나 없앨때
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Album",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/searchAlbum")
    public List<AlbumDto> searchAlbum(@RequestBody AlbumDto album) {//postid가져오면
        return postService.searchAlbum(album.getAlbumName());
    }

    //앨범이름으로 찾기
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Artist",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/searchArtist")
    public List<AlbumDto> searchArtist(@RequestBody ArtistDto artist) {//postid가져오면
        return postService.searchArtist(artist.getArtistName());
    }
    //아티스트이름으로 찾기
}
