package org.cosmic.backend.domain.bestAlbum.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.bestAlbum.applications.BestAlbumService;
import org.cosmic.backend.domain.bestAlbum.dtos.*;
import org.cosmic.backend.domain.playList.dtos.ArtistDto;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.bestAlbum.exceptions.ExistBestAlbumException;
import org.cosmic.backend.domain.bestAlbum.exceptions.NotMatchBestAlbumException;

import java.util.List;

/**
 * BestAlbumApi는 사용자의 좋아요 앨범 목록을 관리하고, 앨범과 아티스트를 검색하는 API 엔드포인트를 제공합니다.
 *
 * 이 컨트롤러는 사용자의 좋아요 앨범 목록 조회, 앨범 추가, 좋아요 앨범 목록 저장, 아티스트 및 앨범 검색 기능을 제공합니다.
 *
 */
@RestController
@RequestMapping("/api/bestAlbum")
@ApiCommonResponses
public class BestAlbumApi {
    private final BestAlbumService bestAlbumService;

    public BestAlbumApi(BestAlbumService bestAlbumService) {
        this.bestAlbumService = bestAlbumService;
    }

    /**
     * 사용자가 좋아요 한 앨범 목록을 반환합니다.
     *
     * @param user 사용자의 ID를 포함한 DTO 객체
     * @return 사용자가 좋아요 한 앨범 목록
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     */
    @Transactional
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<BestAlbumGiveDto> bestAlbumGive(@RequestBody UserDto user) {
        return bestAlbumService.open(user.getUserId());
    }

    /**
     * 사용자의 좋아요 앨범 목록에 새 앨범을 추가합니다.
     *
     * @param bestAlbumDto 사용자의 ID와 추가할 앨범의 ID를 포함한 DTO 객체
     * @return 성공 메시지
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     * @throws ExistBestAlbumException 이미 사용자의 좋아요 목록에 해당 앨범이 존재하는 경우 발생합니다.
     */
    @Transactional
    @PostMapping("/add")
    @ApiResponse(responseCode = "404", description = "Not Found User or Album")
    @ApiResponse(responseCode = "409", description = "Exist BestAlbum")
    public ResponseEntity<?> bestAlbumAdd(@RequestBody BestAlbumDto bestAlbumDto) {
        bestAlbumService.add(bestAlbumDto.getUserId(),bestAlbumDto.getAlbumId());
        return ResponseEntity.ok("성공");
    }

    /**
     * 사용자의 좋아요 앨범 목록을 업데이트합니다.
     *
     * @param bestAlbumlistDto 사용자 ID와 새로 저장할 좋아요 앨범 목록을 포함한 DTO 객체
     * @return 성공 메시지
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     * @throws NotMatchBestAlbumException 사용자의 기존 좋아요 앨범 목록과 일치하지 않는 경우 발생합니다.
     */
    @Transactional
    @PostMapping("/save")
    @ApiResponse(responseCode = "400", description = "Not Match BestAlbum")
    @ApiResponse(responseCode = "404", description = "Not Found User or Album")
    public ResponseEntity<?> bestAlbumSave(@RequestBody BestAlbumListDto bestAlbumlistDto) {
        bestAlbumService.save(bestAlbumlistDto.getUserId(),bestAlbumlistDto.getBestalbum());
        return ResponseEntity.ok("성공");
    }

    /**
     * 주어진 아티스트 이름으로 모든 앨범 정보를 검색합니다.
     *
     * @param artist 아티스트의 이름을 포함한 DTO 객체
     * @return 해당 아티스트가 가진 앨범들의 정보
     * @throws NotFoundArtistException 아티스트를 찾을 수 없는 경우 발생합니다.
     */
    @PostMapping("/Artistsearch")
    @ApiResponse(responseCode = "404", description = "Not Match Artist Name")
    public List<AlbumGiveDto> artistSearch(@RequestBody ArtistDto artist) {
        return bestAlbumService.searchArtist(artist.getArtistName());
    }

    /**
     * 주어진 앨범 제목으로 모든 앨범 정보를 검색합니다.
     *
     * @param album 앨범의 제목을 포함한 DTO 객체
     * @return 해당 제목을 가진 모든 앨범들의 정보
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     */
    @PostMapping("/Albumsearch")
    @ApiResponse(responseCode = "404", description = "Not Match Album Title")
    public List<AlbumGiveDto> albumSearch(@RequestBody AlbumDto album) {
        return bestAlbumService.searchAlbum(album.getAlbumName());
    }
    //앨범 찾기 앨범이름
}
