package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.favoriteArtist.dtos.ArtistData;
import org.cosmic.backend.domain.playList.domains.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album,Long> {
    List<Album> findAllByArtist_ArtistId(Long artistId);
    List<Album> findAllByArtist_ArtistName(String artistName);
    List<Album> findAllByTitle(String title);
    Optional<Album> findByTitleAndArtist_ArtistId(String title, Long artistId);
    Optional<Album> findByTitleAndArtist_ArtistName(String title, String artistName);
    Optional<Album> findByAlbumIdAndArtist_ArtistId(Long albumId,Long artistId);
    @Query("SELECT A.album " +
            "FROM AlbumUser A W" +
            "HERE A.user.userId = :userId ")
    List<Album> findAlbumByUserId(Long userId);

    @Query("SELECT new org.cosmic.backend.domain.favoriteArtist.dtos.ArtistData(A.artist.artistId, A.title, A.cover, A.artist.artistName, A.createdDate) FROM Album A WHERE A.artist.artistName = :artistName")
    List<ArtistData> findAllArtistDataByArtistId(String artistName);
}
