package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumNameDto {
    private String albumName;
    public static AlbumNameDto createAlbumNameDto(String albumName) {
        return  AlbumNameDto.builder()
                .albumName(albumName)
                .build();
    }
}
