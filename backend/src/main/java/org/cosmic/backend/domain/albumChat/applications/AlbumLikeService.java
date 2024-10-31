package org.cosmic.backend.domain.albumChat.applications;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumLikeRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.globals.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>AlbumLikeService 클래스는 앨범 챗의 좋아요 기능과 관련된 비즈니스 로직을 처리합니다.</p>
 *
 * <p>이 서비스는 앨범에 대한 좋아요의 생성, 조회, 삭제 기능을 제공합니다.</p>
 */
@Service
@RequiredArgsConstructor
public class AlbumLikeService {

  private final UsersRepository usersRepository;
  private final AlbumRepository albumRepository;
  private final AlbumLikeRepository albumLikeRepository;

  /**
   * <p>특정 앨범에 대한 좋아요 목록을 조회합니다.</p>
   *
   * @param spotifyAlbumId 조회할 앨범의 ID
   * @return 좋아요 목록을 포함한 리스트
   * @throws NotFoundAlbumChatException 앨범을 찾을 수 없을 때 발생합니다.
   */
  public AlbumDetail getAlbumChatAlbumLikeBySpotifyAlbumId(String spotifyAlbumId) {
    Album album = albumRepository.findBySpotifyAlbumId(spotifyAlbumId)
        .orElseThrow(NotFoundAlbumException::new);
    return AlbumDetail.from(album);
  }

  @Transactional
  public void like(String spotifyAlbumId, Long userId) {
    albumLikeRepository.save(AlbumLike.from(albumRepository.findBySpotifyAlbumId(spotifyAlbumId)
            .orElseThrow(NotFoundAlbumException::new),
        usersRepository.findById(userId).orElseThrow(NotFoundUserException::new)));
  }

  @Transactional
  public void unlike(String spotifyAlbumId, Long userId) {
    AlbumLike albumLike = albumLikeRepository.findByAlbum_SpotifyAlbumIdAndUser_UserId(
        spotifyAlbumId, userId).orElseThrow(() -> new NotFoundException("not found like"));
    albumLikeRepository.delete(albumLike);
  }
}
