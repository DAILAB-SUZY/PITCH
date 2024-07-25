package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ArtistData {
    //해당 이름 가진 아티스트 정보들을 줌
    private Long artistId;
    private String albumName;
    private String cover;
    private String artistName;
    private Instant time;

    public ArtistData(Long artistId, String albumName,Instant time, String cover ) {
        this.artistId = artistId;
        this.albumName = albumName;
        this.cover = cover;
        this.time = time;
    }
}
