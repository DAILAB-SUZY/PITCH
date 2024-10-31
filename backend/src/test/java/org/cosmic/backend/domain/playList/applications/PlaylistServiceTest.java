package org.cosmic.backend.domain.playList.applications;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

  @Mock
  private UsersRepository usersRepository;
  @Mock
  private TrackRepository trackRepository;
  @Mock
  private SearchService searchService;
  @Mock
  private PlaylistRepository playlistRepository;

  @InjectMocks
  private PlaylistService playlistService;

  @Test
  void getAlbumDetail() {
    when(searchService.findAndSaveAlbumBySpotifyId(anyString())).thenReturn(
        Album.builder().albumId(1L).title("album").albumCover("album").artist(
                Artist.builder().artistName("artist").build()).genre("genre").spotifyAlbumId("123456")
            .build());

    AlbumDetail actualValue = playlistService.getAlbumDetail("123456");

    Assertions.assertEquals(1L, actualValue.getAlbumId());
  }
}