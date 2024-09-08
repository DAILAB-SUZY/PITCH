package org.cosmic.backend.domain.bestAlbum.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BestAlbumListDto {
    private List<BestAlbumDetail> bestalbum;
    public static BestAlbumListDto createBestAlbumListDto(List<BestAlbumDetail> bestalbum) {
        return  BestAlbumListDto.builder()
                .bestalbum(bestalbum)
                .build();
    }
}
