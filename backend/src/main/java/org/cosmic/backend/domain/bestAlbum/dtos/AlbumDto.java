package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDto {
    private String albumName;
    public static AlbumDto createAlbumDto(String albumName) {
        return  AlbumDto.builder()
                .albumName(albumName)
                .build();
    }
}
