package org.cosmic.backend.domain.playList;

import org.cosmic.backend.AbstractContainerBaseTest;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PlaylistRepositoryTest extends AbstractContainerBaseTest {

  @Autowired
  private PlaylistRepository playlistRepository;

  @Autowired
  private UsersRepository usersRepository;
  
}
