package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.dtos.ArtistDto;
import org.cosmic.backend.domain.post.applications.PostService;
import org.cosmic.backend.domain.post.dtos.Post.*;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@ApiCommonResponses
public class PostApi {
    private final PostService postService;

    public PostApi(PostService postService) {
        this.postService = postService;
    }

    @Transactional
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<PostReq> giveAllPosts(@RequestBody UserDto user) {
        return postService.getAllPosts(user.getUserId());
    }

    @Transactional
    @PostMapping("/open")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    public PostReq getPostById(@RequestBody PostDto post) {//Post가 아니라 post에서 나와야할 내용들이 리턴되야함
        return postService.getPostById(post.getPostId());
    }
    //특정 post열 때
    @PostMapping("/create")
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    public PostDto createPost(@RequestBody CreatePost post) {//Post로 내용이 추가됨
        return postService.createPost(post);
    }

    //포스트 하나 만들 때
    @PostMapping("/update")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    public ResponseEntity<?> updatePost(@RequestBody UpdatePost post) {//Post로 내용이 추가됨
        postService.updatePost(post);
        return ResponseEntity.ok("성공");
    }
    //업데이트할 때
    @PostMapping("/delete")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    public ResponseEntity<?> deletePost(@RequestBody PostDto post) {//postid가져오면
        postService.deletePost(post.getPostId());
        return ResponseEntity.ok("성공");
    }
    //포스트 하나 없앨때
    @PostMapping("/searchAlbum")
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    public List<AlbumDto> searchAlbum(@RequestBody AlbumDto album) {//postid가져오면
        return postService.searchAlbum(album.getAlbumName());
    }

    //앨범이름으로 찾기
    @PostMapping("/searchArtist")
    @ApiResponse(responseCode = "404", description = "Not Found Artist")
    public List<AlbumDto> searchArtist(@RequestBody ArtistDto artist) {//postid가져오면
        return postService.searchArtist(artist.getArtistName());
    }
    //아티스트이름으로 찾기
}
