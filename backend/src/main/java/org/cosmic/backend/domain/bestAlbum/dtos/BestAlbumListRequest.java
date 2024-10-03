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
public class BestAlbumListRequest {
    private List<BestAlbumDetail> bestalbum;
    public static BestAlbumListRequest createBestAlbumListDto(List<BestAlbumDetail> bestalbum) {
        return  BestAlbumListRequest.builder()
                .bestalbum(bestalbum)
                .build();
    }
}
