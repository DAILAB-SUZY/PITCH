package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatService;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>AlbumChatApi 클래스는 앨범챗 페이지와 관련된 API를 제공합니다.</p>
 *
 * <p>이 API를 통해 사용자는 특정 앨범의 앨범챗을 열고, 좋아요 순으로 댓글을 정렬하는 등의 기능을 수행할 수 있습니다.</p>
 *
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "앨범 챗 관련 API", description = "앨범 챗 댓글/대댓글/좋아요 제공")
public class AlbumChatApi {

    private final AlbumChatService albumChatService;

    /**
     * <p>AlbumChatApi 생성자입니다.</p>
     *
     * @param albumChatService 앨범챗 관련 비즈니스 로직을 처리하는 서비스 클래스
     */
    public AlbumChatApi(AlbumChatService albumChatService) {
        this.albumChatService = albumChatService;
    }

    /**
     * <p>특정 앨범의 앨범챗을 엽니다.</p>
     *
     * @param albumId 앨범 정보를 포함한 DTO 객체
     * @return 해당 앨범의 앨범챗 데이터를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundAlbumChatException 앨범챗을 찾을 수 없을 때 발생합니다.
     */
    @Transactional
    @GetMapping("/open/album/{albumId}")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChat")
    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
        ,implementation= AlbumChatDetail.class))})

    @Operation(summary = "앨범챗 조회",description = "특정 앨범의 앨범챗 조회")
    public ResponseEntity<AlbumChatDetail> getAlbumChatById(
            @Parameter(description = "앨범id")
            @PathVariable("albumId") Long albumId) {
        return ResponseEntity.ok(albumChatService.getAlbumChatById(albumId));
    }

    @Transactional
    @GetMapping("/albumChat")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = AlbumChatDetail.class))))
    @Operation(summary = "앨범챗홈 페이지",description = "앨범 챗 많은 순으로 album정보 띄우기")
    public ResponseEntity<List<AlbumDetail>> albumChatHome(
        @Parameter(description = "페이지 수")
        @RequestParam int page,
        @Parameter(description = "제공량")
        @RequestParam int limit
        )
    {
        return ResponseEntity.ok(albumChatService.albumChatHome(page,limit));
    }


}
