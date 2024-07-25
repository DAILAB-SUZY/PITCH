package org.cosmic.backend.domain.bestAlbum.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.bestAlbum.dtos.*;
import org.cosmic.backend.domain.bestAlbum.applications.BestAlbumService;
import org.cosmic.backend.domain.playList.dto.ArtistDto;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bestAlbum")
public class BestAlbumApi {
    @Autowired
    private BestAlbumService bestAlbumService;
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
    public List<BestAlbumGiveDto> bestAlbumGive(@RequestBody UserDto user) {
        return bestAlbumService.open(user.getUserId());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "404",
            description = "Not Found User or Album",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )
    @Transactional
    @PostMapping("/add")
    public ResponseEntity<?> bestAlbumAdd(@RequestBody BestAlbumDto bestAlbumDto) {
        bestAlbumService.add(bestAlbumDto.getUserId(),bestAlbumDto.getAlbumId());
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
            description = "Not Match BestAlbum",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        ),

        @ApiResponse(responseCode = "404",
            description = "Not Found User or Album",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )
    @Transactional
    @PostMapping("/save")
    public ResponseEntity<?> bestAlbumSave(@RequestBody BestAlbumListDto bestAlbumlistDto) {
        bestAlbumService.save(bestAlbumlistDto.getUserId(),bestAlbumlistDto.getBestalbum());
        return ResponseEntity.ok("성공");

    }
    //앨범 위치 조정

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
    public List<AlbumGiveDto> artistSearch(@RequestBody ArtistDto artist) {
        return bestAlbumService.searchArtist(artist.getArtistName());
    }
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "404",
            description = "Not Match Album Title",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )

    @PostMapping("/Albumsearch")
    public List<AlbumGiveDto> albumSearch(@RequestBody AlbumDto album) {
        return bestAlbumService.searchAlbum(album.getAlbumName());
    }
    //앨범 찾기 앨범이름
}
