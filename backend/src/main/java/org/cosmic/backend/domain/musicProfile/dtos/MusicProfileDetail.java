package org.cosmic.backend.domain.musicProfile.dtos;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.FollowDto;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MusicProfileDetail {

  private UserDetail userDetail;
  private FavoriteArtistDetail favoriteArtist;
  private List<BestAlbumDetail> bestAlbum;
  private List<PlaylistDetail> playlist;
  private List<FollowDto> followings;
  private List<FollowDto> followers;
  private List<PlaylistDetail> recommendation;

  public static MusicProfileDetail from(User user) {
    return MusicProfileDetail.builder()
        .userDetail(UserDetail.from(user))
        .favoriteArtist(FavoriteArtistDetail.from(user.getFavoriteArtist()))
        .bestAlbum(user.getBestAlbums().stream().map(BestAlbumDetail::from).toList())
        .playlist(
            user.getPlaylist().getPlaylist_track().stream().map(PlaylistDetail::from).toList())
        .followings(user.getFollowings().stream().map(FollowDto::following).toList())
        .followers(user.getFollowers().stream().map(FollowDto::follower).toList())
        .build();
  }

}
