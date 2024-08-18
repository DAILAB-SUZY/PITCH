package org.cosmic.backend.domain.favoriteArtist.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.favoriteArtist.applications.FavoriteArtistService;
import org.cosmic.backend.domain.favoriteArtist.dtos.*;
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
@RequestMapping("/api/favoriteArtist")
@ApiCommonResponses
public class FavoriteApi {
    private final FavoriteArtistService favoriteartistService;

    public FavoriteApi(FavoriteArtistService favoriteartistService) {
        this.favoriteartistService = favoriteartistService;
    }

    @PostMapping("/give")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public FavoriteArtistDto favoriteArtistGiveData(@RequestBody UserDto user) {
        return favoriteartistService.favoriteArtistGiveData(user.getUserId());
    }

    @PostMapping("/searchartist")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found Artist")
    public List<ArtistData> artistSearchData(@RequestBody ArtistDto artist) {
        return favoriteartistService.artistSearchData(artist.getArtistName());
    }

    @PostMapping("/searchalbum")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    public List<AlbumData> albumSearchData(@RequestBody AlbumRequest album) {
        return favoriteartistService.albumSearchData(album.getArtistId(), album.getAlbumName());
    }

    @PostMapping("/searchtrack")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found Track")
    public TrackData trackSearchData(@RequestBody TrackRequest track) {
        return favoriteartistService.trackSearchData(track.getAlbumId(), track.getTrackName());
    }

    @PostMapping("/save")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User Or Track Or Album Or Artist")
    public ResponseEntity<?> favoriteArtistSaveData(@RequestBody FavoriteReq favoriteartist) {
        favoriteartistService.favoriteArtistSaveData(favoriteartist);
        return ResponseEntity.ok("성공");
    }
}
