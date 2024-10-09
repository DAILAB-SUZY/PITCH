package org.cosmic.backend.domainsTest.user;

import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private final User user = User.builder()
      .userId(1L)
      .username("testman")
      .email(Email.builder().email("test@example.com").build())
      .dna1(MusicDna.builder().name("rock").dnaId(1L).build())
      .dna2(MusicDna.builder().name("hip-hop").dnaId(2L).build())
      .dna3(MusicDna.builder().name("pop").dnaId(3L).build())
      .profilePicture("base")
      .create_time(Instant.now())
      .build();
  private final Long id = 1L;
  private final UserService userService = new UserService();
  @Mock
  private UsersRepository usersRepository;

  @BeforeEach
  public void setUp() {
    when(usersRepository.findById(id)).thenReturn(Optional.of(user));
    userService.setUsersRepository(usersRepository);
  }

  @Test
  @DisplayName("유저 정보 요청 테스트")
  public void userInfoTest() {
    Assertions.assertEquals("testman", userService.getUserDetail(id).getUsername());
  }
}
