package org.cosmic.backend.domainsTest.bestAlbum;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.bestAlbum.applications.BestAlbumService;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumRequest;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class BestAlbumServiceTest {

  private final User user = User.builder()
      .userId(1L)
      .bestAlbums(List.of())
      .build();
  @Mock
  UsersRepository userRepository;
  @Mock
  SearchService searchService;
  @InjectMocks
  BestAlbumService bestAlbumService;

  private Album makeAlbum(String id) {
    return Album.builder()
        .albumId(Long.parseLong(id))
        .title(id)
        .albumCover(id)
        .spotifyAlbumId(id)
        .build();
  }

  private BestAlbumRequest makeRequest(String id) {
    return new BestAlbumRequest(id, (int) (Math.random() * 100) % 5);
  }

  private BestAlbumDetail makeDetail(String id, int score) {
    return BestAlbumDetail.builder()
        .spotifyId(id)
        .score(score)
        .build();
  }

  @BeforeEach
  public void setUp() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
  }

  @Test
  public void bestAlbumSaveTest() {
    List<Album> albums = IntStream.rangeClosed(1, 2)
        .mapToObj(id -> makeAlbum(String.valueOf(id)))
        .toList();
    albums.forEach(album -> {
      when(
          searchService.findAndSaveAlbumBySpotifyId(String.valueOf(album.getAlbumId()))).thenReturn(
          album);
    });
    when(userRepository.save(user))
        .thenReturn(User.builder()
            .bestAlbums(albums.stream()
                .map(album -> UserBestAlbum.builder()
                    .album(album)
                    .user(user)
                    .build())
                .toList())
            .build()
        );
    List<BestAlbumRequest> newBestAlbums = List.of(makeRequest("1"), makeRequest("2"));

    List<BestAlbumDetail> actualValues = bestAlbumService.save(1L, newBestAlbums);

    Assertions.assertEquals(2, actualValues.size());
  }
}