package org.cosmic.backend.domain.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.dtos.Image;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SpotifySearchArtistResponse {
    private String artistId;
    private String imageUrl;
    private String name;
}
