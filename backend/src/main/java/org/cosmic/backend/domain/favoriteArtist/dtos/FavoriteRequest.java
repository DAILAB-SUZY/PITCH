package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRequest {
    private Long artistId;
    private Long albumId;
    private String cover;
    private Long trackId;
    public static FavoriteRequest createFavoriteReq(Long artistId, Long albumId, Long trackId, String cover) {
        return  FavoriteRequest.builder()
                .artistId(artistId)
                .albumId(albumId)
                .trackId(trackId)
                .cover(cover)
                .build();
    }
}
