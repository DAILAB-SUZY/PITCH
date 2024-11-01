package org.cosmic.backend.domain.playList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

  @Mock
  private SearchService searchService;

  @InjectMocks
  private PlaylistService playlistService;

  @Mock
  private TrackRepository trackRepository;
  @Mock
  private UsersRepository usersRepository;

  @Test
  @DisplayName("spotify AlbumId로 엘범 디테일로 나오는지 확인")
  void getAlbumDetail() {
    when(searchService.findAndSaveAlbumBySpotifyId(anyString())).thenReturn(
        Album.builder().albumId(1L).title("album").albumCover("album").artist(
                Artist.builder().artistName("artist").build()).genre("genre").spotifyAlbumId("123456")
            .build());

    AlbumDetail actualValue = playlistService.getAlbumDetail("123456");

    Assertions.assertEquals(1L, actualValue.getAlbumId());
  }

  @Test
  @DisplayName("노래 추천 확인")
  void getRecommendationTest() {
    playlistService.recommendation();

    verify(trackRepository).findRandomTracks(5);
  }

  @Test
  @DisplayName("팔로잉한 사람들의 플레이리스트 조회")
  void getFollowingPlaylistTest() {
    User user = mock(User.class);

    when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

    playlistService.followingPlaylists(1L);

    verify(user).getFollowings();
  }
}