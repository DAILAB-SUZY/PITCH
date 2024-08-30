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
    private Long userId;
    private List<BestAlbumDetail> bestalbum;
    public BestAlbumListDto(Long userId) {
        this.userId = userId;
    }
    public static BestAlbumListDto createBestAlbumListDto(Long userId,List<BestAlbumDetail> bestalbum) {
        return  BestAlbumListDto.builder()
                .userId(userId)
                .bestalbum(bestalbum)
                .build();
    }
}
