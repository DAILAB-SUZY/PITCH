package org.cosmic.backend.domain.playList.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.playList.dtos.*;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.globals.dto.ErrorResponse;
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

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "404",
            description = "Not Found User",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )

    @Transactional
    @PostMapping("/give")
    public List<PlaylistGiveDto> dataGive(@RequestBody UserDto user) {
       return playlistService.open(user.getUserId());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "404",
            description = "Not Found User or Track",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )

    @PostMapping("/save")//수정한 플레이리스트를 여기 저장
    @Transactional
    public ResponseEntity<?> savePlaylistData(@RequestBody PlaylistDto playlist) {
        Long Key= playlist.getId();
        playlistService.save(Key,playlist.getPlaylist());
        return ResponseEntity.ok("성공");
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "400",
            description = "Not Match Artist Name",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )

    @PostMapping("/Artistsearch")
    @Transactional
    public List<TrackGiveDto> artistSearch(@RequestBody ArtistDto artist) {
        return playlistService.artistSearch(artist.getArtistName());
    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "404",
            description = "Not Match Track Title",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )

    @PostMapping("/Tracksearch")
    @Transactional
    public List<TrackGiveDto> trackSearch(@RequestBody TrackDto track) {
        return playlistService.trackSearch(track.getTrackName());
    }
}
