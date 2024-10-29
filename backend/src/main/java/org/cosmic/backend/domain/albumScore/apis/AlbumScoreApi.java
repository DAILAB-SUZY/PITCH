package org.cosmic.backend.domain.albumScore.apis;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.albumScore.applications.AlbumScoreService;
import org.cosmic.backend.domain.albumScore.dtos.AlbumScoreDto;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@Tag(name = "베스트 앨범 관련 API", description = "베스트 앨범 정보 제공 및 저장")
public class AlbumScoreApi {
    @Autowired
    private AlbumScoreService albumScoreService;

    @PostMapping("/album/{albumId}/score")
    public AlbumDetail addScore(
       @PathVariable long albumId,
       @RequestBody AlbumScoreDto albumScoreDto,
       @AuthenticationPrincipal Long userId) {
        //특정 앨범에서 점수매기기
        return albumScoreService.addScore(albumId,userId,albumScoreDto.getScore());
    }

    @GetMapping("/user/{userId}/score")
    public List<AlbumDetail> getScore(@PathVariable long userId) {
        //특정 유저의 점수 매긴 앨범들 다 가져옴
        return albumScoreService.getScore(userId);
    }
}
