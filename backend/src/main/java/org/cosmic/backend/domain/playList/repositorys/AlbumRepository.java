package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.favoriteArtist.dtos.ArtistDetail;
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
    @Query("SELECT A.album FROM org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum A WHERE A.user.userId = :userId ")
    List<Album> findAlbumByUserId(Long userId);

    @Query("SELECT new org.cosmic.backend.domain.favoriteArtist.dtos.ArtistDetail(A.artist.artistId, A.albumCover, A.artist.artistName) FROM Album A WHERE A.artist.artistName = :artistName")
    List<ArtistDetail> findAllArtistDataByArtistId(String artistName);
}
