package org.cosmic.backend.domain.playList.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.user.domains.Follow;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerPlaylistDetail {

  Long playlistId;
  List<String> albumCover;
  UserDetail author;

  public FollowerPlaylistDetail(Playlist playlist) {
    this.playlistId = playlist.getPlaylistId();
    this.author = User.toUserDetail(playlist.getUser());
  }

  public static FollowerPlaylistDetail from(Follow follow) {
    Playlist playlist = follow.getOther().getPlaylist();
    return FollowerPlaylistDetail.builder()
        .playlistId(playlist.getPlaylistId())
        .author(UserDetail.from(follow.getOther()))
        .albumCover(playlist.getPlaylist_track().stream()
            .map(playlistTrack -> playlistTrack.getTrack().getTrackCover()).toList())
        .build();
  }
}
