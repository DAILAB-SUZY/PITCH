package org.cosmic.backend.domains.music;

import org.cosmic.backend.domain.music.domain.Genre;
import org.cosmic.backend.domain.music.domain.Tracks;
import org.cosmic.backend.domain.music.repositories.GenreRepository;
import org.cosmic.backend.domain.music.repositories.TracksRepository;
import org.cosmic.backend.testcontainer.TestContainerBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

@Testcontainers
@SpringBootTest
public class GenreInSongTest extends TestContainerBase {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void createGenreInSong(){
        Genre pop = genreRepository.findByName("POP").orElseThrow(RuntimeException::new);
        Genre rock = genreRepository.findByName("ROCK").orElseThrow(RuntimeException::new);
        Tracks tracks = tracksRepository.save(Tracks.builder()
                .name("tt")
                .duration(22.0f)
                .instrumental(false)
                .build());

        tracks.getGenres().addAll(Set.of(rock, pop));
        tracksRepository.save(tracks);

        Assertions.assertTrue(tracks.getGenres().containsAll(Set.of(rock, pop)));
    }
}
