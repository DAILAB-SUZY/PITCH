package org.cosmic.backend.domain.favoriteArtist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class favoriteArtistDTO {
    private String artistName;
    private String albumName;
    private String cover;
    private String trackName;
    private Long userId;
}
