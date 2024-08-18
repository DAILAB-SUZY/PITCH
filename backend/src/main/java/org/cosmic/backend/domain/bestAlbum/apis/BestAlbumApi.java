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

import java.util.List;

@RestController
@RequestMapping("/api/bestAlbum")
@ApiCommonResponses
public class BestAlbumApi {
    private final BestAlbumService bestAlbumService;

    public BestAlbumApi(BestAlbumService bestAlbumService) {
        this.bestAlbumService = bestAlbumService;
    }

    @Transactional
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<BestAlbumGiveDto> bestAlbumGive(@RequestBody UserDto user) {
        return bestAlbumService.open(user.getUserId());
    }

    @Transactional
    @PostMapping("/add")
    @ApiResponse(responseCode = "404", description = "Not Found User or Album")
    @ApiResponse(responseCode = "409", description = "Exist BestAlbum")
    public ResponseEntity<?> bestAlbumAdd(@RequestBody BestAlbumDto bestAlbumDto) {
        bestAlbumService.add(bestAlbumDto.getUserId(),bestAlbumDto.getAlbumId());
        return ResponseEntity.ok("성공");
    }

    @Transactional
    @PostMapping("/save")
    @ApiResponse(responseCode = "400", description = "Not Match BestAlbum")
    @ApiResponse(responseCode = "404", description = "Not Found User or Album")
    public ResponseEntity<?> bestAlbumSave(@RequestBody BestAlbumListDto bestAlbumlistDto) {
        bestAlbumService.save(bestAlbumlistDto.getUserId(),bestAlbumlistDto.getBestalbum());
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/Artistsearch")
    @ApiResponse(responseCode = "404", description = "Not Match Artist Name")
    public List<AlbumGiveDto> artistSearch(@RequestBody ArtistDto artist) {
        return bestAlbumService.searchArtist(artist.getArtistName());
    }

    @PostMapping("/Albumsearch")
    @ApiResponse(responseCode = "404", description = "Not Match Album Title")
    public List<AlbumGiveDto> albumSearch(@RequestBody AlbumDto album) {
        return bestAlbumService.searchAlbum(album.getAlbumName());
    }
    //앨범 찾기 앨범이름
}
