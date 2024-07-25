package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BestAlbumGiveDto {
    Long albumId;
    String albumName;
    String cover;
    public BestAlbumGiveDto(Long albumId, String albumName, String cover) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.cover = cover;
    }
}
