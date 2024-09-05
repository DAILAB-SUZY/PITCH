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
    private Long albumId;
    public static AlbumDto createAlbumDto(Long albumId) {
        return  AlbumDto.builder()
                .albumId(albumId)
                .build();
    }
}
