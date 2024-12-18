package org.cosmic.backend.domainsTest.user;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.cosmic.backend.domain.user.applications.FollowService;
import org.cosmic.backend.domain.user.domains.Follow;
import org.cosmic.backend.domain.user.domains.FollowPK;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.FollowRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

  @InjectMocks
  private FollowService followService;
  @Mock
  private FollowRepository followRepository;
  @Mock
  private UsersRepository usersRepository;

  @Test
  @DisplayName("팔로우 하기")
  public void followTest() {
    User user = createUser(1L);
    User other = createUser(2L);
    Follow follow = Follow.builder().user(user).other(other).build();
    when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
    when(usersRepository.findById(2L)).thenReturn(Optional.of(other));
    when(followRepository.save(follow)).thenReturn(follow);

    followService.follow(1L, 2L);
    verify(followRepository).save(follow);
  }

  @Test
  @DisplayName("언팔로우 하기")
  public void unfollowTest() {
    followService.unfollow(1L, 2L);
    verify(followRepository).deleteById(FollowPK.builder().user(1L).other(2L).build());
  }

  private User createUser(Long userId) {
    return User.builder().userId(userId).build();
  }
  
}
