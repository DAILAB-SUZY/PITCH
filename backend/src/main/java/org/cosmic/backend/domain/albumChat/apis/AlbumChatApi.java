package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatService;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albumchat")
@ApiCommonResponses
public class AlbumChatApi {
    private final AlbumChatService albumChatService;

    public AlbumChatApi(AlbumChatService albumChatService) {
        this.albumChatService = albumChatService;
    }

    @Transactional
    @PostMapping("/open")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChat")
    public AlbumChatResponse getAlbumChatById(@RequestBody AlbumDto album) {
        return albumChatService.getAlbumChatById(album);
    }

    @Transactional
    @PostMapping("/manylike")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChat")
    public List<AlbumChatCommentResponse> getAlbumChatCommentByManyLikeId(@RequestBody AlbumChatDto albumchat) {
        return albumChatService.getAlbumChatCommentByManyLikeId(albumchat);
    }
}