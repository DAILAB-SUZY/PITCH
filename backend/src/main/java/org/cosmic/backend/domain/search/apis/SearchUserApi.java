package org.cosmic.backend.domain.search.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.search.applications.SearchUserService;
import org.cosmic.backend.domain.search.dtos.OtherUserFollowDetail;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.user.dtos.UserDetail;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search/user")
@ApiCommonResponses
@Tag(name = "유저 검색 관련 API", description = "유저 이름 검색 데이터 제공")
public class SearchUserApi {
    @Autowired
    SearchUserService searchUserService;

    @GetMapping("/{name}")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = OtherUserFollowDetail.class))))
    @ApiResponse(responseCode = "404", description = "Not Found User")
    @Operation(summary = "유저 조회",description = "유저 이름 검색을 통한 유저들 조회")
    public ResponseEntity<List<OtherUserFollowDetail>> searchAlbum(
            @Parameter(description = "유저 이름")
            @PathVariable String name){
        return ResponseEntity.ok(
                searchUserService.searchUser(name));
    }
}
