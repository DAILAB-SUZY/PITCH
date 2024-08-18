package org.cosmic.backend.domain.playList.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.playList.dtos.*;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistApi {
    private final PlaylistService playlistService;

    public PlaylistApi(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Transactional
    @PostMapping("/give")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<PlaylistGiveDto> dataGive(@RequestBody UserDto user) {
        return playlistService.open(user.getUserId());
    }

    @PostMapping("/save")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User or Track")
    public ResponseEntity<?> savePlaylistData(@RequestBody PlaylistDto playlist) {
        Long key = playlist.getId();
        playlistService.save(key, playlist.getPlaylist());
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/Artistsearch")
    @Transactional
    @ApiResponse(responseCode = "400", description = "Not Match Artist Name")
    public List<TrackGiveDto> artistSearch(@RequestBody ArtistDto artist) {
        return playlistService.artistSearch(artist.getArtistName());
    }

    @PostMapping("/Tracksearch")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Match Track Title")
    public List<TrackGiveDto> trackSearch(@RequestBody TrackDto track) {
        return playlistService.trackSearch(track.getTrackName());
    }
}
