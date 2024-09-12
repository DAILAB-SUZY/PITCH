package org.cosmic.backend.domain.playList.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.playList.dtos.PlaylistDto;
import org.cosmic.backend.domain.playList.dtos.PlaylistGiveDto;
import org.cosmic.backend.domain.playList.dtos.TrackGiveDto;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 플레이리스트 관련 API를 제공하는 REST 컨트롤러입니다.
 * 사용자 플레이리스트의 조회, 저장 및 아티스트/트랙 검색 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/")
public class PlaylistApi {
    private final PlaylistService playlistService;

    /**
     * PlaylistApi의 생성자입니다.
     *
     * @param playlistService 플레이리스트 관련 비즈니스 로직을 처리하는 서비스 클래스
     */
    public PlaylistApi(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /**
     * 사용자 ID로 플레이리스트 데이터를 조회합니다.
     *
     * @param userId 사용자 정보를 포함한 DTO 객체
     * @return 플레이리스트 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     */
    @Transactional
    @GetMapping("/playlist")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<PlaylistGiveDto> dataGive(@PathVariable Long userId) {
        return playlistService.open(userId);
    }

    /**
     * 플레이리스트 데이터를 저장합니다.
     *
     * @param playlist 저장할 플레이리스트 데이터를 포함한 DTO 객체
     * @return 저장 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     * @throws NotFoundTrackException 트랙을 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/playlist")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User or Track")
    public ResponseEntity<?> savePlaylistData(@RequestBody PlaylistDto playlist,@AuthenticationPrincipal Long userId) {
        playlistService.save(userId, playlist.getPlaylist());
        return ResponseEntity.ok("성공");
    }

    /**
     * 아티스트 이름으로 트랙을 검색합니다.
     *
     * @param artistName 검색할 아티스트 정보를 포함한 DTO 객체
     * @return 해당 아티스트의 트랙 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundArtistException 아티스트 이름이 일치하지 않을 때 발생합니다.
     */
    @GetMapping("/playlist/artist/{artistName}")
    @Transactional
    @ApiResponse(responseCode = "400", description = "Not Match Artist Name")
    public List<TrackGiveDto> artistSearch(@PathVariable String artistName) {
        return playlistService.artistSearch(artistName);
    }

    /**
     * 트랙 제목으로 트랙을 검색합니다.
     *
     * @param trackName 검색할 트랙 정보를 포함한 DTO 객체
     * @return 해당 트랙 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundTrackException 트랙 제목이 일치하지 않을 때 발생합니다.
     */
    @GetMapping("/playlist/track/{trackName}")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Match Track Title")
    public List<TrackGiveDto> trackSearch(@PathVariable String trackName) {
        return playlistService.trackSearch(trackName);
    }
}
