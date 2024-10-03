package org.cosmic.backend.domain.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifySearchTrackResponse {
    private SpotifySearchArtistResponse trackArtist;//노래의 아티스트
    private SpotifySearchAlbumResponse album;
    private String trackId;
    private String trackName;
    private String duration;
}
