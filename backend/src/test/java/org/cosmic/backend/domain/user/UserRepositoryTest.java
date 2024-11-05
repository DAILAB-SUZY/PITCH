package org.cosmic.backend.domain.user;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.AbstractContainerBaseTest;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.ServerProperty;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;

@Log4j2
@Import(ServerProperty.class)
public class UserRepositoryTest extends AbstractContainerBaseTest {

  @Value("${server.origin}")
  private String serverOrigin;

  @Autowired
  private UsersRepository usersRepository;

  @Test
  @DisplayName("유저 생성 확인")
  public void userSaveTest() {
    Email email = creator.createAndSaveEmail("test@example.com");

    usersRepository.save(User.builder().email(email).username("test").password("1234").build());

    User user = usersRepository.findByEmail_Email(email.getEmail())
        .orElseThrow(NotFoundUserException::new);
    Assertions.assertNotNull(user.getId());
    Assertions.assertNotNull(user.getPlaylist());
    Assertions.assertNotNull(user.getFavoriteArtist());
  }

  @Test
  @DisplayName("유저 디테일에 이미지 주소 확인")
  public void userListFindTest() {
    User user = creator.createAndSaveUser("testman");

    log.info("user: {}", user.getOriginProfilePicture());
    Assertions.assertTrue(user.getOriginProfilePicture().contains(serverOrigin));
  }
}