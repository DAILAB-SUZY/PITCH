package org.cosmic.backend.domain.playList.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.dto.*;
import org.cosmic.backend.domain.playList.service.PlaylistService;
import org.cosmic.backend.domain.user.dto.userDto;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
public class PlatlistApi {

    @Autowired
    private PlaylistService playlistService;

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
    public List<playlistGiveDto> giveData(@RequestBody userDto user) {
        //없는 유저 아이디일 때 오류 발생
        //유저의 플렝
       return playlistService.open(user.getUserid());

    }//특정 플레이어의 플레이리스트 가져와서 줌

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
    public ResponseEntity<?> savePlaylistData(@RequestBody playlistDTO playlist) {
        // 데이터 받을 때
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

            @ApiResponse(responseCode = "404",
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
    public List<TrackGiveDto> searchArtist(@RequestBody ArtistDTO artist) {
        return playlistService.searchArtist(artist.getArtistName());
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
    public List<TrackGiveDto> searchTrack(@RequestBody trackDTO track) {
        return playlistService.searchTrack(track.getTrackName());
    }

}
