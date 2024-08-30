package org.cosmic.backend.domain.playList.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.playList.dtos.*;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 플레이리스트 관련 API를 제공하는 REST 컨트롤러입니다.
 * 사용자 플레이리스트의 조회, 저장 및 아티스트/트랙 검색 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/playlist")
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
     * @param user 사용자 정보를 포함한 DTO 객체
     * @return 플레이리스트 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     */
    @Transactional
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<PlaylistGiveDto> dataGive(@RequestBody UserDto user) {
        return playlistService.open(user.getUserId());
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
    @PostMapping("/save")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User or Track")
    public ResponseEntity<?> savePlaylistData(@RequestBody PlaylistDto playlist) {
        Long key = playlist.getId();
        playlistService.save(key, playlist.getPlaylist());
        return ResponseEntity.ok("성공");
    }

    /**
     * 아티스트 이름으로 트랙을 검색합니다.
     *
     * @param artist 검색할 아티스트 정보를 포함한 DTO 객체
     * @return 해당 아티스트의 트랙 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundArtistException 아티스트 이름이 일치하지 않을 때 발생합니다.
     */
    @PostMapping("/Artistsearch")
    @Transactional
    @ApiResponse(responseCode = "400", description = "Not Match Artist Name")
    public List<TrackGiveDto> artistSearch(@RequestBody ArtistDto artist) {
        return playlistService.artistSearch(artist.getArtistName());
    }

    /**
     * 트랙 제목으로 트랙을 검색합니다.
     *
     * @param track 검색할 트랙 정보를 포함한 DTO 객체
     * @return 해당 트랙 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundTrackException 트랙 제목이 일치하지 않을 때 발생합니다.
     */
    @PostMapping("/Tracksearch")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Match Track Title")
    public List<TrackGiveDto> trackSearch(@RequestBody TrackDto track) {
        return playlistService.trackSearch(track.getTrackName());
    }
}
