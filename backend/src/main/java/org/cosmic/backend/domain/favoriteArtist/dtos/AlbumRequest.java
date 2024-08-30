package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRequest {
    private Long artistId;
    private String albumName;
    public static AlbumRequest createAlbumRequest(Long artistId,String albumName) {
        return  AlbumRequest.builder()
                .artistId(artistId)
                .albumName(albumName)
                .build();
    }
}
