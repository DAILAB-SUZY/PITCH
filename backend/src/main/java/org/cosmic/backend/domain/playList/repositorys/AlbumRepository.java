package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.favoriteArtist.dtos.ArtistDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {
    List<Album> findAllByArtist_ArtistId(Long artistId);
    List<Album> findAllByArtist_ArtistName(String artistName);
    List<Album> findAllByTitle(String title);
    Optional<Album> findByTitleAndArtist_ArtistId(String title, Long artistId);
    Optional<Album> findByTitleAndArtist_ArtistName(String title, String artistName);
    Optional<Album> findBySpotifyAlbumIdAndArtist_SpotifyArtistId(String spotifyAlbumId,String spotifyArtistId);
    @Query("SELECT A.album FROM org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum A WHERE A.user.userId = :userId ")
    List<Album> findAlbumByUserId(Long userId);

    @Query("SELECT new org.cosmic.backend.domain.favoriteArtist.dtos.ArtistDetail(A.artist.artistId, A.albumCover, A.artist.artistName) FROM Album A WHERE A.artist.artistName = :artistName")
    List<ArtistDetail> findAllArtistDataByArtistId(String artistName);

    @Query("SELECT a FROM Album a LEFT JOIN a.albumChatComments acc GROUP BY a.albumId ORDER BY COUNT(acc) DESC,a.albumId ASC ")
    Page<Album> findAlbumsOrderByCommentCount(Pageable pageable);

    @Query(value = "SELECT a.album_id, album_cover, created_date, genre, spotify_album_id, title, artist_id\n" +
            "FROM album a\n" +
            "LEFT JOIN album_like al\n" +
            "ON a.album_id=al.album_id\n" +
            "GROUP BY a.album_id\n" +
            "ORDER BY COUNT(al) DESC, a.album_id", nativeQuery = true)
    Page<Album> findAlbumsOrderByAlbumLikeCount(Pageable pageable);

    Optional<Album> findBySpotifyAlbumId(String spotifyAlbumId);
}
