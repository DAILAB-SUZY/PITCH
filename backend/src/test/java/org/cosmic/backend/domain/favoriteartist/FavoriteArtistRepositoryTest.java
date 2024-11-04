package org.cosmic.backend.domain.favoriteartist;

import org.cosmic.backend.AbstractContainerBaseTest;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.repositorys.FavoriteArtistRepository;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class FavoriteArtistRepositoryTest extends AbstractContainerBaseTest {

  @Autowired
  private FavoriteArtistRepository favoriteArtistRepository;

  @Test
  @DisplayName("FavoriteArtist 저장 확인")
  @Transactional
  public void favoriteArtistSaveTest() {
    User user = creator.createAndSaveUser("favoriteMan");
    Track track = creator.createAndSaveTracks("myMusic");
    FavoriteArtist favoriteArtist = favoriteArtistRepository.findByUser_UserId(
        user.getUserId()).orElseThrow(NotFoundUserException::new);

    Assertions.assertDoesNotThrow(() -> favoriteArtist.setArtist(track.getArtist()));
    Assertions.assertDoesNotThrow(() -> favoriteArtist.setAlbum(track.getAlbum()));
    Assertions.assertDoesNotThrow(() -> favoriteArtist.setTrack(track));
  }
}
