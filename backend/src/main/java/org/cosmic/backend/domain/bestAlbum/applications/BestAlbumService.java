package org.cosmic.backend.domain.bestAlbum.applications;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumRequest;
import org.cosmic.backend.domain.bestAlbum.exceptions.ExistBestAlbumException;
import org.cosmic.backend.domain.bestAlbum.exceptions.NotMatchBestAlbumException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

/**
 * <p> BestAlbumService는 사용자가 좋아하는 앨범을 관리하는 서비스입니다. </p>
 *
 * <p> 이 서비스는 사용자의 좋아요 앨범 목록을 조회, 추가, 저장하고, 아티스트 또는 앨범을 검색하는 기능을 제공합니다. </p>
 */
@Service
@RequiredArgsConstructor
public class BestAlbumService {

  private final UsersRepository usersRepository;
  private final SearchService searchService;

  /**
   * 사용자가 좋아요 한 앨범 목록을 반환합니다.
   *
   * @param userId 사용자의 ID
   * @return 사용자가 좋아요 한 앨범 목록
   * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
   */
  public List<BestAlbumDetail> open(Long userId) {
    User user = usersRepository.findById(userId).orElseThrow(NotFoundUserException::new);

    return getBestAlbumDetailList(user);
  }

  private List<BestAlbumDetail> getBestAlbumDetailList(User user) {
    return BestAlbumDetail.from(user.getBestAlbums());
  }

  /**
   * 사용자의 좋아요 앨범 목록에 새 앨범을 추가합니다.
   *
   * @param score          추가할 앨범의 점수
   * @param userId         사용자의 ID
   * @param spotifyAlbumId 추가할 앨범의 ID
   * @return 업데이트된 사용자의 좋아요 앨범 목록
   * @throws NotFoundUserException   사용자를 찾을 수 없는 경우 발생합니다.
   * @throws NotFoundAlbumException  앨범을 찾을 수 없는 경우 발생합니다.
   * @throws ExistBestAlbumException 사용자의 좋아요 목록에 해당 앨범이 이미 존재하는 경우 발생합니다.
   */
  @Transactional
  public List<BestAlbumDetail> add(Integer score, Long userId, String spotifyAlbumId) {
    User user = usersRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    user.getBestAlbums().add(
        UserBestAlbum.from(user, searchService.findAndSaveAlbumBySpotifyId(spotifyAlbumId), score));

    User updatedUser = usersRepository.save(user);
    return getBestAlbumDetailList(updatedUser);
  }

  private List<UserBestAlbum> getNewBestAlbums(List<BestAlbumRequest> bestAlbumList, User user) {
    return bestAlbumList.stream()
        .map(bestAlbum -> UserBestAlbum.from(user,
            searchService.findAndSaveAlbumBySpotifyId(bestAlbum.spotifyAlbumId()),
            bestAlbum.score()))
        .toList();
  }

  /**
   * 사용자의 좋아요 앨범 목록을 업데이트합니다. 기존 목록을 삭제하고 새 목록을 저장합니다.
   *
   * @param userId        사용자의 ID
   * @param bestAlbumList 새로 저장할 앨범 목록
   * @return 업데이트된 사용자의 좋아요 앨범 목록
   * @throws NotFoundUserException      사용자를 찾을 수 없는 경우 발생합니다.
   * @throws NotFoundAlbumException     앨범을 찾을 수 없는 경우 발생합니다.
   * @throws NotMatchBestAlbumException 사용자의 기존 좋아요 앨범 목록과 일치하지 않는 경우 발생합니다.
   */
  @Transactional
  public List<BestAlbumDetail> save(Long userId, List<BestAlbumRequest> bestAlbumList) {
    User user = usersRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    user.getBestAlbums().clear();
    usersRepository.saveAndFlush(user);
    user.getBestAlbums().addAll(getNewBestAlbums(bestAlbumList, user));
    User updatedUser = usersRepository.save(user);
    return getBestAlbumDetailList(updatedUser);
  }
}
