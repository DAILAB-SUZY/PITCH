package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteArtistDetail {
    private String artistName;
    private String albumName;
    private String trackName;
    private String cover;
    //좋아하는 아티스트, 앨범, 노래 커버 주기.
}
