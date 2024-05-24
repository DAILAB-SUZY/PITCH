package org.cosmic.backend.domains.music;

import org.cosmic.backend.domain.music.domain.Genre;
import org.cosmic.backend.domain.music.domain.Tracks;
import org.cosmic.backend.domain.music.repositories.TracksRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class GenreInSongTest {
    @Autowired
    private TracksRepository tracksRepository;

    @Test
    public void createGenreInSong(){
        Tracks tracks = tracksRepository.save(Tracks.builder()
                .name("tt")
                .genre(Genre.CLASSIC)
                .duration(22.0f)
                .instrumental(false)
                .build());

        Assertions.assertEquals(tracks.getGenre(), Genre.CLASSIC);
    }
}
