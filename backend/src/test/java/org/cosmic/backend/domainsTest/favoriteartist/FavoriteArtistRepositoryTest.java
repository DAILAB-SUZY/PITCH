package org.cosmic.backend.domainsTest.favoriteartist;

import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.repositorys.FavoriteArtistRepository;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domainsTest.Creator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Log4j2
@Import(Creator.class)
public class FavoriteArtistRepositoryTest {

  @Autowired
  private Creator creator;

  @Autowired
  private FavoriteArtistRepository favoriteArtistRepository;

  private User user;

  @BeforeEach
  public void setUp() {
    user = creator.createAndSaveUsers(1).get(0);
    Track track = creator.createAndSaveTracks(0, 1).get(0);

    favoriteArtistRepository.save(FavoriteArtist.builder()
        .user(user)
        .artist(track.getArtist())
        .album(track.getAlbum())
        .track(track)
        .build());
  }

  @Test
  @Transactional
  public void favoriteArtistSaveTest() {
    Optional<FavoriteArtist> favoriteArtist = favoriteArtistRepository.findByUser_UserId(
        user.getUserId());

    Assertions.assertFalse(favoriteArtist.isEmpty());
  }

  @Test
  @Transactional
  public void favoriteArtistModifyTest() {
    FavoriteArtist favoriteArtist = favoriteArtistRepository.findByUser_UserId(
        user.getUserId()).orElseThrow(NotFoundUserException::new);

    Track newTrack = creator.createAndSaveTracks(1, 5).get(2);

    favoriteArtist.setArtist(newTrack.getArtist());
    favoriteArtistRepository.save(favoriteArtist);
    favoriteArtist.setAlbum(newTrack.getAlbum());
    favoriteArtist.setTrack(newTrack);

    favoriteArtistRepository.save(favoriteArtist);

    FavoriteArtist updated = favoriteArtistRepository.findByUser_UserId(
        user.getUserId()).orElseThrow(NotFoundUserException::new);

    Assertions.assertEquals(newTrack.getArtist(), updated.getArtist());
  }
}
