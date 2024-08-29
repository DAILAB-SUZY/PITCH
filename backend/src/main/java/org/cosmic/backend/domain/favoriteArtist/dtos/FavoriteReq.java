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
    private Long userId;
    public static FavoriteReq createFavoriteReq(Long userId, Long artistId,Long albumId,Long trackId,String cover) {
        return  FavoriteReq.builder()
                .userId(userId)
                .artistId(artistId)
                .albumId(albumId)
                .trackId(trackId)
                .cover(cover)
                .build();
    }
}
