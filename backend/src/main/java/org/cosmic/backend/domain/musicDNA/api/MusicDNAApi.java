package org.cosmic.backend.domain.musicDNA.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDNA.applications.MusicDNAService;
import org.cosmic.backend.domain.musicDNA.dto.DnaDTO;
import org.cosmic.backend.domain.musicDNA.dto.ListDNA;
import org.cosmic.backend.domain.musicDNA.dto.UserDnaResponse;
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
@RequestMapping("/api/dna")
public class MusicDNAApi {
    @Autowired
    private MusicDNAService musicDNAService;

    @Transactional
    @PostMapping("/give")
    public List<ListDNA> giveDNAData() {
        return musicDNAService.getAllDna();
    }

    @PostMapping("/save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Need 4 MusicDna",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "Not Found Emotion",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
        }
    )
    public ResponseEntity<?> saveUserDNAData(@RequestBody DnaDTO dna) {
        // 데이터 받을 때
        Long Key= dna.getKey();
        musicDNAService.saveDNA(Key, dna.getDna());//Long
        return ResponseEntity.ok("성공");//회원가입 완료 표시
    }

    @PostMapping("/info")
    @Transactional
    public List<UserDnaResponse> giveUserDNA(@RequestBody userDto user) {
        System.out.println(user);
        return musicDNAService.getUserDna(user);
    }

}
