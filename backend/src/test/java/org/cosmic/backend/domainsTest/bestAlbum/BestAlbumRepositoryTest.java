package org.cosmic.backend.domainsTest.bestAlbum;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.Creator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Log4j2
@Import(Creator.class)
public class BestAlbumRepositoryTest {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private Creator creator;

  @Test
  @Transactional
  public void userBestAlbumDefaultTest() {
    User user = creator.createAndSaveUsers(1).get(0);

    Assertions.assertEquals(user.getBestAlbums().size(), 0);
  }

  @Test
  @Transactional
  public void userAddBestAlbumTest() {
    User user = creator.createAndSaveUsers(1).get(0);
    List<UserBestAlbum> newBestAlbums = creator.createAndSaveUserBestAlbums(user, 10);

    user.getBestAlbums().addAll(newBestAlbums);

    usersRepository.save(user);

    Assertions.assertEquals(user.getBestAlbums().size(), 10);
  }

  @Test
  @Transactional
  public void userRetainAlbumTest() {
    User user = creator.createAndSaveUsers(1).get(0);
    List<UserBestAlbum> newBestAlbums = creator.createAndSaveUserBestAlbums(user, 10);
    user.getBestAlbums().addAll(newBestAlbums);
    usersRepository.save(user);

    user.getBestAlbums().clear();
    usersRepository.save(user);

    user.getBestAlbums().addAll(creator.createAndSaveUserBestAlbums(user, 11, 15));
    user.getBestAlbums().addAll(newBestAlbums);
    usersRepository.save(user);
    Assertions.assertEquals(user.getBestAlbums().size(), 14);
  }
}
