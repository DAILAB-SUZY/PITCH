package org.cosmic.backend.domain.favoriteArtist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumData {
    private Long albumId;
    private String trackName;
}
