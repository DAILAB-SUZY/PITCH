package org.cosmic.backend.domain.musicProfile.apis;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.musicProfile.applications.MusicProfileService;
import org.cosmic.backend.domain.user.dtos.MusicProfileDetail;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "뮤직 프로필 관련 API", description = "뮤직 프로필 정보 제공")
public class MusicProfileApi {
    @Autowired
    private MusicProfileService musicProfileService;

    /**
     * <p>특정 사용자의 뮤직 프로필을 조회합니다.</p>
     *
     * @param userId 조회할 사용자의 ID
     * @return 사용자의 뮤직 프로필 데이터를 포함한 {@link ResponseEntity}
     */
    @PostMapping("/user/{userId}/musicProfile")
    public ResponseEntity<MusicProfileDetail> userMusicProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(musicProfileService.openMusicProfile(userId));
    }
}
