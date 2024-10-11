package org.cosmic.backend.domain.musicProfile.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDetail {
    private FavoriteArtistDetail favoriteArtist;
    private List<BestAlbumDetail> bestAlbum;
}
