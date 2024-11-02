package org.cosmic.backend.domain.albumScore.applications;

import jakarta.transaction.Transactional;
import java.util.List;
import org.cosmic.backend.domain.albumScore.domains.AlbumScore;
import org.cosmic.backend.domain.albumScore.repositorys.AlbumScoreRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class AlbumScoreService {

  private final AlbumScoreRepository albumScoreRepository;
  private final UsersRepository usersRepository;
  private final AlbumRepository albumRepository;

  public AlbumScoreService(AlbumScoreRepository albumScoreRepository,
      UsersRepository usersRepository, AlbumRepository albumRepository) {
    this.albumScoreRepository = albumScoreRepository;
    this.usersRepository = usersRepository;
    this.albumRepository = albumRepository;
  }

  @Transactional
  public AlbumDetail addScore(Long albumId, Long userId, Integer score) {
    createAlbumScore(albumId, userId, score);
    return getScore(albumId, userId);
  }

  public AlbumDetail getScore(Long albumId, Long userId) {
    return AlbumDetail.from(albumScoreRepository.findByAlbum_AlbumIdAndUser_UserId(albumId, userId)
        .orElseThrow(NotFoundAlbumException::new));
  }

  public List<AlbumDetail> getScores(Long userId) {
    //해당 유저의 모든 점수 매긴 앨범들 가져오기.
    usersRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    return AlbumDetail.fromByAlbumScore(albumScoreRepository.findByUser_UserId(userId));
  }

  @Transactional
  protected void deleteAlbumScore(Long albumId, Long userId) {
    albumScoreRepository.deleteByAlbum_AlbumIdAndUser_UserId(albumId, userId);
  }

  @Transactional
  public void createAlbumScore(Long albumId, Long userId, Integer score) {
    createAlbumScore(albumRepository.findById(albumId).orElseThrow(NotFoundAlbumException::new),
        userId, score);
  }

  @Transactional
  public void createAlbumScore(String spotifyAlbumId, Long userId, Integer score) {
    createAlbumScore(albumRepository.findBySpotifyAlbumId(spotifyAlbumId)
            .orElseThrow(NotFoundAlbumException::new),
        userId, score);
  }

  private AlbumScore getOrCreateAlbumScore(Album album, Long userId) {
    User user = usersRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    return albumScoreRepository.findByAlbumAndUser(album, user)
        .orElseGet(() -> AlbumScore.from(user, album, 0));
  }

  @Transactional
  protected void createAlbumScore(Album album, Long userId, Integer score) {
    if (score.equals(0)) {
      deleteAlbumScore(album.getAlbumId(), userId);
      return;
    }
    AlbumScore albumScore = getOrCreateAlbumScore(album, userId);
    albumScore.setScore(score);
    albumScoreRepository.save(albumScore);
  }
}
