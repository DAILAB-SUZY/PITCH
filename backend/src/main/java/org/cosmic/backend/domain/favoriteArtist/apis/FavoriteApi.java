package org.cosmic.backend.domain.favoriteArtist.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.favoriteArtist.applications.FavoriteArtistService;
import org.cosmic.backend.domain.favoriteArtist.dtos.*;
import org.cosmic.backend.domain.playList.dtos.ArtistDto;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FavoriteApi는 사용자의 즐겨찾기 아티스트와 관련된 REST API를 제공합니다.
 *
 * 이 API는 사용자의 즐겨찾기 아티스트 정보를 조회하고, 아티스트, 앨범, 트랙을 검색하며,
 * 사용자의 즐겨찾기 정보를 저장하는 기능을 제공합니다.
 *
 */
@RestController
@RequestMapping("/api/favoriteArtist")
@ApiCommonResponses
public class FavoriteApi {
    private final FavoriteArtistService favoriteartistService;

    public FavoriteApi(FavoriteArtistService favoriteartistService) {
        this.favoriteartistService = favoriteartistService;
    }

    /**
     * 사용자가 즐겨찾는 아티스트 정보를 반환합니다.
     *
     * @param userId 사용자의 ID를 포함하는 DTO 객체
     * @return 사용자가 즐겨찾는 아티스트 정보
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     */
    @GetMapping("/give/{userId}")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public FavoriteArtistDto favoriteArtistGiveData(@PathVariable Long userId) {
        return favoriteartistService.favoriteArtistGiveData(userId);
    }

    /**
     * 주어진 아티스트 이름으로 관련된 앨범 및 트랙 데이터를 검색합니다.
     *
     * @param artistName 아티스트의 이름을 포함하는 DTO 객체
     * @return 해당 아티스트의 앨범 및 트랙 데이터 리스트
     * @throws NotFoundArtistException 아티스트를 찾을 수 없는 경우 발생합니다.
     */
    @GetMapping("/searchartist/{artistName}")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found Artist")
    public List<ArtistData> artistSearchData(@PathVariable String artistName) {
        return favoriteartistService.artistSearchData(artistName);
    }

    /**
     * 주어진 앨범 이름과 아티스트 ID로 관련된 트랙 데이터를 검색합니다.
     *
     * @param artistId,albumName 앨범의 이름과 아티스트의 ID를 포함하는 요청 객체
     * @return 해당 앨범의 트랙 데이터 리스트
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     */
    @GetMapping("/searchalbum/{artistId}")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    public List<AlbumData> albumSearchData(@PathVariable Long artistId,@RequestBody AlbumRequest album) {
        return favoriteartistService.albumSearchData(artistId,album.getAlbumName());
    }

    /**
     * 주어진 앨범 ID와 트랙 이름으로 트랙 데이터를 검색합니다.
     *
     * @param albumId,trackName 앨범의 ID와 트랙의 이름을 포함하는 요청 객체
     * @return 해당 트랙의 데이터
     * @throws NotFoundTrackException 트랙을 찾을 수 없는 경우 발생합니다.
     */
    @GetMapping("/searchtrack/{albumId}")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found Track")
    public TrackData trackSearchData(@PathVariable Long albumId, @RequestBody TrackRequest track) {
        return favoriteartistService.trackSearchData(albumId, track.getTrackName());
    }

    /**
     * 사용자의 즐겨찾기 아티스트 정보를 저장합니다.
     *
     * @param favoriteartist 사용자의 즐겨찾기 아티스트 정보를 포함하는 요청 객체
     * @return 성공 메시지를 포함하는 응답
     * @throws NotFoundUserException 사용자 정보를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundTrackException 트랙 정보를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범 정보를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundArtistException 아티스트 정보를 찾을 수 없는 경우 발생합니다.
     */
    @PostMapping("/save")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User Or Track Or Album Or Artist")
    public ResponseEntity<?> favoriteArtistSaveData(@RequestBody FavoriteReq favoriteartist) {
        favoriteartistService.favoriteArtistSaveData(favoriteartist);
        return ResponseEntity.ok("성공");
    }
}
