package org.cosmic.backend.domain.musicProfile.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.musicProfile.applications.MusicProfileService;
import org.cosmic.backend.domain.musicProfile.dtos.ActivityDetail;
import org.cosmic.backend.domain.musicProfile.dtos.MusicProfileDetail;
import org.cosmic.backend.domain.musicProfile.dtos.ProfileDetail;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @GetMapping("/user/{userId}/musicProfile")
    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
            ,implementation= MusicProfileDetail.class))})
    @Operation(summary = "뮤직 프로필 API", description = "특정 유저의 뮤직 프로필 조회")
    public ResponseEntity<MusicProfileDetail> userMusicProfile(
            @Parameter(description = "유저 id")
            @PathVariable Long userId) {
        return ResponseEntity.ok(musicProfileService.openMusicProfile(userId));
    }

    @GetMapping("/user/{userId}/musicProfile/activity")
    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
            ,implementation= MusicProfileDetail.class))})
    @Operation(summary = "뮤직 프로필 activity탭", description = "특정 유저의 activity만 제공")
    public ResponseEntity<ActivityDetail> activityOpen(
            @Parameter(description = "유저 id")
            @PathVariable Long userId) {
        return ResponseEntity.ok(musicProfileService.openActivity(userId));
    }

    @GetMapping("/user/{userId}/musicProfile/profile")
    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
            ,implementation= MusicProfileDetail.class))})
    @Operation(summary = "뮤직 프로필 profile탭", description = "특정 유저의 profile만 제공")
    public ResponseEntity<ProfileDetail> profileOpen(
            @Parameter(description = "유저 id")
            @PathVariable Long userId) {
        return ResponseEntity.ok(musicProfileService.openProfile(userId));
    }

}
