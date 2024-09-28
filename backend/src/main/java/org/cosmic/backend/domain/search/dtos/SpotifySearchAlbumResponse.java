package org.cosmic.backend.domain.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.dtos.Image;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifySearchAlbumResponse {
    private List<SpotifySearchArtistResponse> artists;
    private String albumId;
    private List<Image> images;
    private String name;
    private String release_date;
}
