package org.cosmic.backend.domain.playList.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Playlist_Track;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistDetail {

  Long trackId;
  String title;
  String artistName;
  String trackCover;

  public static PlaylistDetail from(Playlist_Track playlistTrack) {
    return PlaylistDetail.builder()
        .trackId(playlistTrack.getTrack().getTrackId())
        .title(playlistTrack.getTrack().getTitle())
        .artistName(playlistTrack.getTrack().getArtist().getArtistName())
        .trackCover(playlistTrack.getTrack().getTrackCover())
        .build();
  }
}
