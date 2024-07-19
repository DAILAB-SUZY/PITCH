package org.cosmic.backend.domain.favoriteArtist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistData {
    //해당 이름 가진 아티스트 정보들을 줌
    private Long artistId;
    private String albumName;
    private String cover;
    private String artistName;
    private Instant time;
}
