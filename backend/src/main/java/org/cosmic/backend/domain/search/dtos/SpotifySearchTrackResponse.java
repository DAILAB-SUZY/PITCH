package org.cosmic.backend.domain.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.dtos.Image;

import java.util.List;

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
