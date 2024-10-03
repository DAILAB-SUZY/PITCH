package org.cosmic.backend.domain.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifySearchAlbumResponse {
    private SpotifySearchArtistResponse albumArtist;//앨범의 아티스트
    private String albumId;
    private String imageUrl;
    private String name;
    private Integer total_tracks;
    private String release_date;
}
