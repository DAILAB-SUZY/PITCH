package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteArtistDetail {
    private String artistName;
    private String albumName;
    private String trackName;
    private String artistCover;
    private String albumCover;
    private String trackCover;
}
