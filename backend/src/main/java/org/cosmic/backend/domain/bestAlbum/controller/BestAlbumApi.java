package org.cosmic.backend.domain.bestAlbum.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.bestAlbum.dto.*;
import org.cosmic.backend.domain.bestAlbum.service.BestAlbumService;
import org.cosmic.backend.domain.playList.dto.ArtistDTO;
import org.cosmic.backend.domain.playList.dto.playlistGiveDto;
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
@RequestMapping("/api/bestAlbum")
public class BestAlbumApi {

    @Autowired
    private BestAlbumService bestAlbumService;
    private playlistGiveDto newplaylistGiveDto = new playlistGiveDto();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "401",
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
    public List<BestAlbumGiveDto> giveData(@RequestBody userDto user) {
        return bestAlbumService.open(user.getUserid());
    }
    //사용자의 앨범정보 가져오는 함수

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "401",
                    description = "Not Found User",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<?> addData(@RequestBody BestAlbumDto bestAlbumDto) {
        bestAlbumService.add(bestAlbumDto.getUserId(),bestAlbumDto.getAlbumId());
        return ResponseEntity.ok("성공");
    }
    //사용자가 앨범을 선택해서 등록 버튼을 누르면 1개의 앨범이 추가됨(기존 리스트 뒷부분에 추가)

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "401",
                    description = "Not Found User",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<?> saveData(@RequestBody BestAlbumListDTO bestAlbumlistDto) {
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

            @ApiResponse(responseCode = "401",
                    description = "Not Match Artist Name",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )

    @PostMapping("/Artistsearch")
    public List<AlbumGiveDto> searchArtist(@RequestBody ArtistDTO artist) {
        return bestAlbumService.searchArtist(artist.getArtistName());
    }
    //앨범 찾기 아티스트이름



    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "401",
                    description = "Not Match Album Title",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )

    @PostMapping("/Albumsearch")
    public List<AlbumGiveDto> searchAlbum(@RequestBody AlbumDTO album) {
        return bestAlbumService.searchAlbum(album.getAlbumName());
    }
    //앨범 찾기 앨범이름
}
