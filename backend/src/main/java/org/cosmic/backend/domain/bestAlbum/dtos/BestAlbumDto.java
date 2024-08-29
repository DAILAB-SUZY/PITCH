package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BestAlbumDto {
    Long userId;
    Long albumId;
    public static BestAlbumDto createBestAlbumDto(Long userId,Long albumId) {
        return  BestAlbumDto.builder()
                .userId(userId)
                .albumId(albumId)
                .build();
    }
}
