package org.cosmic.backend.domain.albumScore.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.albumScore.applications.AlbumScoreService;
import org.cosmic.backend.domain.albumScore.dtos.AlbumScoreDto;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@Tag(name = "앨범 평점 관련 API", description = "특정 유저의 앨범 평점들 제공 및 평점 저장")
public class AlbumScoreApi {
    @Autowired
    private AlbumScoreService albumScoreService;

    @PostMapping("/album/{albumId}/score")
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
            ,implementation= AlbumDetail.class))})
    @Operation(summary = "특정 앨범에 평점 추가", description = "유저가 특정 앨범을 선택해 평점을 저장합니다.")
    public AlbumDetail addScore(
       @PathVariable long albumId,
       @RequestBody AlbumScoreDto albumScoreDto,
       @AuthenticationPrincipal Long userId) {
        //특정 앨범에서 점수매기기
        return albumScoreService.addScore(albumId,userId,albumScoreDto.getScore());
    }

    @GetMapping("/user/{userId}/score")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = AlbumDetail.class))))
    @Operation(summary = "특정 유저의 앨범 평점 제공", description = "특정 유저가 평점을 매긴 앨범들 정보를 제공합니다.")
    public List<AlbumDetail> getScore(@PathVariable long userId) {
        //특정 유저의 점수 매긴 앨범들 다 가져옴
        return albumScoreService.getScore(userId);
    }
}
