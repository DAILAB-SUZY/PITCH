package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AlbumChatApi 클래스는 앨범챗 페이지와 관련된 API를 제공합니다.
 * 사용자는 이 API를 통해 앨범챗 열기, 좋아요 순으로 댓글 정렬 등의 기능을 수행할 수 있습니다.
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
public class AlbumChatApi {
    private final AlbumChatService albumChatService;
    /**
     * AlbumChatApi 생성자.
     *
     * @param albumChatService AlbumChatService 주입
     */
    public AlbumChatApi(AlbumChatService albumChatService) {
        this.albumChatService = albumChatService;
    }

    /**
     * 앨범 챗 ID를 기반으로 해당 앨범 챗의 댓글을 좋아요 수 순서대로 정렬하여 반환합니다.
     *
     * @param albumId 조회할 앨범 ID
     * @return List<AlbumChatCommentResponse> 좋아요 수 순서로 정렬된 앨범 챗 댓글 목록
     * @throws NotFoundAlbumChatException 특정 앨범의 앨범 챗을 찾을 수 없는 경우 발생
     */

    @Transactional
    @GetMapping("/album/{albumId}")
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    public List<AlbumChatCommentResponse> getAlbumChatCommentByManyLikeId(
        @PathVariable("albumId")Long albumId,@RequestParam String sorted) {
        if(sorted.equals("manylike"))
        {
            return albumChatService.getAlbumChatCommentByManyLikeId(albumId);
        }
        return null;
    }

}