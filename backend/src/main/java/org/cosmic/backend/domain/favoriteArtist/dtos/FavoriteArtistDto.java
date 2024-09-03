package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteArtistDto {
    private String artistName;
    private String albumName;
    private String trackName;
    private String cover;
}
