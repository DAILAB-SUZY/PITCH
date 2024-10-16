package org.cosmic.backend.domain.playList.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Playlist_Track;
import org.cosmic.backend.domain.playList.domains.Track;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistDetail {

  Long trackId;
  String title;
  String artistName;
  Long albumId;
  String trackCover;

  public static PlaylistDetail from(Playlist_Track playlistTrack) {
    return PlaylistDetail.builder()
        .trackId(playlistTrack.getTrack().getTrackId())
        .title(playlistTrack.getTrack().getTitle())
        .artistName(playlistTrack.getTrack().getArtist().getArtistName())
        .albumId(playlistTrack.getTrack().getAlbum().getAlbumId())
        .trackCover(playlistTrack.getTrack().getTrackCover())
        .build();
  }

  public static PlaylistDetail from(Track track) {
    return PlaylistDetail.builder()
        .trackId(track.getTrackId())
        .title(track.getTitle())
        .artistName(track.getArtist().getArtistName())
        .albumId(track.getAlbum().getAlbumId())
        .trackCover(track.getTrackCover())
        .build();
  }
}
