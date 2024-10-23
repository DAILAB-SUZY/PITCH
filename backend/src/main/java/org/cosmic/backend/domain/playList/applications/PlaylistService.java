package org.cosmic.backend.domain.playList.applications;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.playList.domains.Playlist_Track;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.FollowerPlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.SpotifyTracksDto;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

/**
 * <p>플레이리스트 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.</p>
 *
 * <p>사용자의 플레이리스트를 저장, 조회하며, 아티스트 및 트랙 검색 기능을 제공합니다.</p>
 */
@Service
@RequiredArgsConstructor
public class PlaylistService {

  private final PlaylistRepository playlistRepository;
  private final UsersRepository usersRepository;
  private final TrackRepository trackRepository;
  private final SearchService searchService;

  private Track getOrFindTrackBySpotifyId(String spotifyId) {
    return trackRepository.findBySpotifyTrackId(spotifyId)
        .orElseGet(() -> searchService.findAndSaveTrackBySpotifyId(spotifyId));
  }

  /**
   * <p>사용자의 플레이리스트를 저장 또는 수정합니다.</p>
   *
   * @param userId      사용자 ID
   * @param playlistDto 새롭게 저장할 플레이리스트 세부 정보 목록
   * @return 저장된 플레이리스트 데이터를 포함한 리스트
   * @throws NotFoundUserException  사용자가 존재하지 않을 경우 발생합니다.
   * @throws NotFoundTrackException 플레이리스트에 포함된 트랙이 존재하지 않을 경우 발생합니다.
   */
  @Transactional
  public List<PlaylistDetail> save(Long userId, SpotifyTracksDto playlistDto) {
    Playlist playlist = playlistRepository.findByUser_UserId(userId)
        .orElseThrow(NotFoundUserException::new);
    playlist.getPlaylist_track().clear();
    playlistDto.spotifyTrackIds().stream()
        .map(trackId -> Playlist_Track.builder()
            .track(getOrFindTrackBySpotifyId(trackId))
            .playlist(playlist)
            .build())
        .forEach(playlist.getPlaylist_track()::add);
    for(int i=1;i<=playlist.getPlaylist_track().size();i++) {
      playlist.getPlaylist_track().get(i-1).setTrackOrder(i);
    }
    playlist.setUpdateTime(Instant.now());
    playlistRepository.save(playlist);
    return open(userId);
  }

  /**
   * <p>사용자 ID로 플레이리스트를 조회합니다.</p>
   *
   * @param userId 사용자 ID
   * @return 플레이리스트 데이터를 포함한 리스트
   * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
   */
  public List<PlaylistDetail> open(Long userId) {
    Playlist playlist = playlistRepository.findByUser_UserId(userId)
        .orElseThrow(NotFoundUserException::new);
    return playlist.getPlaylist_track().stream().map(PlaylistDetail::from).toList();
  }

  public List<FollowerPlaylistDetail> followingPlaylists(Long userId) {
    User user = usersRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    return user.getFollowings().stream().map(FollowerPlaylistDetail::from).toList();
  }

  public List<PlaylistDetail> recommendation() {
    return trackRepository.findRandomTracks(5).stream().map(PlaylistDetail::from).toList();
  }
}
