package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumGiveDto {
    String title;
    String artistName;
    String cover;
    public AlbumGiveDto(String title, String artistName, String cover) {
        this.title = title;
        this.artistName = artistName;
        this.cover = cover;
    }
}
