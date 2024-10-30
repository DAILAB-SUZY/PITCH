package org.cosmic.backend.domain.musicProfile.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDetail {

  private FavoriteArtistDetail favoriteArtist;
  private List<BestAlbumDetail> bestAlbum;

  public static ProfileDetail from(User user) {
    return ProfileDetail.builder()
        .favoriteArtist(FavoriteArtistDetail.from(user.getFavoriteArtist()==null ? new FavoriteArtist(): user.getFavoriteArtist()))
        .bestAlbum(BestAlbumDetail.from(user.getBestAlbums()))
        .build();
  }
}
