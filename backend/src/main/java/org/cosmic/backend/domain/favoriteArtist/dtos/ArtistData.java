package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistData {
    //해당 이름 가진 아티스트 정보들을 줌
    private Long artistId;
    private String albumName;
    private String cover;
    private String artistName;
    private Instant time;
}
