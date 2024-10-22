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
    private String spotifyArtistId;
    private String spotifyAlbumId;
    private String spotifyTrackId;

    public static FavoriteRequest createFavoriteReq(String artistId, String albumId, String trackId) {
        return  FavoriteRequest.builder()
                .spotifyArtistId(artistId)
                .spotifyAlbumId(albumId)
                .spotifyTrackId(trackId)
                .build();
    }
}
