package org.cosmic.backend.domain.user.applications;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.Follow;
import org.cosmic.backend.domain.user.dtos.FollowDto;
import org.cosmic.backend.domain.user.repositorys.FollowRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.globals.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;

  private final UsersRepository usersRepository;

  @Transactional
  public Follow follow(Long userId, Long otherUserId) {
    return followRepository.save(Follow.builder()
        .user(usersRepository.findById(userId).orElseThrow(NotFoundUserException::new))
        .other(usersRepository.findById(otherUserId).orElseThrow(NotFoundUserException::new))
        .build());
  }

  @Transactional
  public void unfollow(Long userId, Long otherUserId) {
    Follow follow = followRepository.findByUser_UserIdAndOther_UserId(userId, otherUserId)
        .orElseThrow(() -> new NotFoundException("follow does not exist"));
    followRepository.delete(follow);
  }

  public List<FollowDto> followings(Long userId) {
    return followRepository.findAllByUser_UserId(userId).stream().map(FollowDto::following)
        .toList();
  }

  public List<FollowDto> followers(Long userId) {
    return followRepository.findAllByOther_UserId(userId).stream().map(FollowDto::follower)
        .toList();
  }
}
