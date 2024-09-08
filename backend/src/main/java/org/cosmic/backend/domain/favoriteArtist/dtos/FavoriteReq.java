package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteReq {
    private Long artistId;
    private Long albumId;
    private String cover;
    private Long trackId;
    public static FavoriteReq createFavoriteReq(Long artistId,Long albumId,Long trackId,String cover) {
        return  FavoriteReq.builder()
                .artistId(artistId)
                .albumId(albumId)
                .trackId(trackId)
                .cover(cover)
                .build();
    }
}
