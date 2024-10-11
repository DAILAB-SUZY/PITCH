package org.cosmic.backend.domain.musicProfile.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MusicProfileDetail {

  private UserDetail userDetail;
  private FavoriteArtistDetail favoriteArtist;
  private List<BestAlbumDetail> bestAlbum;
  private List<UserDnaResponse> userDna;
  private List<PlaylistDetail> playlist;
  private List<FollowDto> followings;
  private List<FollowDto> followers;

  public static MusicProfileDetail from(User user) {
    return MusicProfileDetail.builder()
        .userDetail(UserDetail.from(user))
        .favoriteArtist(FavoriteArtistDetail.from(user.getFavoriteArtist()))
        .bestAlbum(user.getBestAlbums().stream().map(BestAlbumDetail::from).toList())
        .userDna(user.getDNAs().stream().map(UserDnaResponse::from).toList())
        .playlist(
            user.getPlaylist().getPlaylist_track().stream().map(PlaylistDetail::from).toList())
        .followings(user.getFollowings().stream().map(FollowDto::following).toList())
        .followers(user.getFollowers().stream().map(FollowDto::follower).toList())
        .build();
  }
}
