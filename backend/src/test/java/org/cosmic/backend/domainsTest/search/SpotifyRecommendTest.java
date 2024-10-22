package org.cosmic.backend.domainsTest.search;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.playList.domains.Playlist_Track;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Log4j2
public class SpotifyRecommendTest {

  @InjectMocks
  private SearchService searchService;
  @Mock
  private UsersRepository usersRepository;
  @Mock
  private TrackRepository trackRepository;

  @Mock
  private Track track;

  @Mock
  private Artist artist;

  @Test
  public void getRecommendTest() {
    when(usersRepository.findById(1L)).thenReturn(Optional.of(
        User.builder()
            .favoriteArtist(FavoriteArtist.builder()
                .artist(Artist.builder().spotifyArtistId("49vOeJAPxAz6YmVZPNM7ys").build())
                .build())
            .playlist(Playlist.builder()
                .playlist_track(Stream.of("5mdl3TlXrImNPrIo3aO70q", "1xlCcSVgFufo88sfkKarhk")
                    .map(id -> Playlist_Track.builder()
                        .track(Track.builder()
                            .spotifyTrackId(id)
                            .build())
                        .build())
                    .toList())
                .build())
            .build()));
    when(trackRepository.findBySpotifyTrackId(anyString())).thenReturn(
        Optional.of(Track.builder()
            .artist(Artist.builder()
                .artistName("wow")
                .build())
            .title("test")
            .album(Album.builder()
                .albumId(1L)
                .build())
            .build()));

    List<PlaylistDetail> recommendations = searchService.getRecommendations(1L);

    Assertions.assertEquals(5, recommendations.size());
  }
}
