package org.cosmic.backend.domain.musicProfile.applications;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.musicProfile.dtos.ActivityDetail;
import org.cosmic.backend.domain.musicProfile.dtos.MusicProfileDetail;
import org.cosmic.backend.domain.musicProfile.dtos.ProfileDetail;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class MusicProfileService {

  @Autowired
  UsersRepository usersRepository;

  /**
   * <p>특정 사용자의 뮤직 프로필을 조회합니다.</p>
   *
   * @param userId 조회할 사용자의 ID
   * @return 사용자의 뮤직 프로필 데이터를 포함한 객체
   */
  public MusicProfileDetail openMusicProfile(Long userId) {
    return MusicProfileDetail.from(
        usersRepository.findById(userId).orElseThrow(NotFoundUserException::new));
  }

  public ProfileDetail openProfile(Long userId) {
    return ProfileDetail.from(
        usersRepository.findById(userId).orElseThrow(NotFoundUserException::new));
  }

  public ActivityDetail openActivity(Long userId) {
    return ActivityDetail.from(
        usersRepository.findById(userId).orElseThrow(NotFoundUserException::new));
  }
}
