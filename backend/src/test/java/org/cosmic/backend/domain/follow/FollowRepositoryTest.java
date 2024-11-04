package org.cosmic.backend.domain.follow;

import java.util.List;
import java.util.stream.IntStream;
import org.cosmic.backend.AbstractContainerBaseTest;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.Follow;
import org.cosmic.backend.domain.user.domains.FollowPK;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.FollowRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class FollowRepositoryTest extends AbstractContainerBaseTest {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private FollowRepository followRepository;

  @Autowired
  private EmailRepository emailRepository;

  private User createAndSaveUser(String username) {
    return usersRepository.save(createUser(username));
  }

  private User createUser(String username) {
    return User.builder()
        .email(emailRepository.save(
            Email.builder().email(username + "@example.com").verificationCode("1234").build()))
        .password("1").username(username).build();
  }

  private List<User> createAndSaveUsers(int size) {
    String baseName = "username";
    return IntStream.range(0, size).mapToObj(idx -> createAndSaveUser(baseName + idx)).toList();
  }

  @Test
  @DisplayName("팔로우 생성 및 조회 확인")
  @Transactional
  public void followCreationTest() {
    User user = createAndSaveUser("testman");
    User other = createAndSaveUser("otherman");

    followRepository.save(Follow.builder().user(user).other(other).build());

    Follow follow = followRepository.findById(
        FollowPK.builder().user(user.getUserId()).other(other.getUserId()).build()).orElseThrow();

    Assertions.assertEquals(user.getUserId(), follow.getUser().getUserId());
    Assertions.assertEquals(other.getUserId(), follow.getOther().getUserId());
  }

  @Test
  @DisplayName("유저에서 팔로우 확인")
  @Transactional
  public void followCheckInUserTest() {
    User user = createAndSaveUser("myuser");
    List<User> others = createAndSaveUsers(100);

    others.forEach(
        other -> followRepository.save(Follow.builder().user(user).other(other).build()));

    List<Follow> follows = followRepository.findAllByUser_UserId(user.getUserId());
    Assertions.assertEquals(100, follows.size());
  }
}
