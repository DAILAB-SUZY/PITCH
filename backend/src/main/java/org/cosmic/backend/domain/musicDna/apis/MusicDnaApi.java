package org.cosmic.backend.domain.musicDna.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDna.applications.MusicDnaService;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.musicDna.dtos.DnaDto;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.musicDna.exceptions.NotMatchMusicDnaCountException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>음악 DNA 관련 API를 제공하는 REST 컨트롤러입니다.</p>
 *
 * <p>이 컨트롤러는 사용자와 관련된 음악 DNA 데이터를 저장하고 조회하는 기능을 포함합니다.</p>
 *
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "MusicDna관련 API", description = "MusicDna 정보 수집 및 제공")
public class MusicDnaApi {

    private final MusicDnaService musicDnaService;

    /**
     * <p>MusicDnaApi의 생성자입니다.</p>
     *
     * @param musicDnaService 음악 DNA 관련 서비스를 처리하는 서비스 클래스
     */
    public MusicDnaApi(MusicDnaService musicDnaService) {
        this.musicDnaService = musicDnaService;
    }

    /**
     * <p>모든 DNA 데이터를 조회합니다.</p>
     *
     * @return 모든 DNA 데이터를 포함하는 리스트
     */
    @GetMapping("/dna")
    @Operation(summary = "dna정보만 제공")
    public ResponseEntity<List<DnaDetail>> DnaGiveData() {
        return ResponseEntity.ok(musicDnaService.getAllDna());
    }

    /**
     * <p>사용자로부터 제공된 DNA 데이터를 저장합니다.</p>
     *
     * @param dna 저장할 DNA 데이터를 포함한 DTO 객체
     * @param userId 사용자 ID (인증된 사용자)
     * @return 저장된 DNA 데이터를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundUserException 유저 정보가 일치하지 않을 때 발생합니다.
     * @throws NotMatchMusicDnaCountException DNA 데이터가 4개 이상일 때 발생합니다.
     */
    @PostMapping("/dna")
    @ApiResponse(responseCode = "400", description = "Need 4 MusicDna")
    @ApiResponse(responseCode = "404", description = "Not Found Emotion")
    @Operation(summary = "dna정보 설정")
    public ResponseEntity<List<UserDnaResponse>> userDnaSaveData(@RequestBody DnaDto dna, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(musicDnaService.saveDNA(userId, dna.getDna()));
    }

    /**
     * <p>특정 사용자의 DNA 데이터를 조회합니다.</p>
     *
     * @param userId 사용자의 ID
     * @return 해당 사용자의 DNA 데이터를 포함한 리스트
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     */
    @GetMapping("/dna/user/{userId}")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User")
    @Operation(summary = "특정 유저의 dna정보만 제공")
    public ResponseEntity<List<UserDnaResponse>> userDnaGive(@PathVariable Long userId) {
        return ResponseEntity.ok(musicDnaService.getUserDna(userId));
    }
}
