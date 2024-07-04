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
    private Long userId;
    private Long artistId;
    private Long albumId;
    private Long trackId;
}
