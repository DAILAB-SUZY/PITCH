package org.cosmic.backend.domain.playList.repositorys;

import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.favoriteArtist.dtos.ArtistDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

  List<Album> findAllByArtist_ArtistId(Long artistId);

  List<Album> findAllByArtist_ArtistName(String artistName);

  List<Album> findAllByTitle(String title);

  Optional<Album> findByTitleAndArtist_ArtistId(String title, Long artistId);

  Optional<Album> findByTitleAndArtist_ArtistName(String title, String artistName);

  Optional<Album> findBySpotifyAlbumIdAndArtist_SpotifyArtistId(String spotifyAlbumId,
      String spotifyArtistId);

  @Query("SELECT A.album FROM org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum A WHERE A.user.userId = :userId ")
  List<Album> findAlbumByUserId(Long userId);

  @Query("SELECT new org.cosmic.backend.domain.favoriteArtist.dtos.ArtistDetail(A.artist.artistId, A.albumCover, A.artist.artistName) FROM Album A WHERE A.artist.artistName = :artistName")
  List<ArtistDetail> findAllArtistDataByArtistId(String artistName);

  @Query("SELECT a FROM Album a LEFT JOIN a.albumChatComments acc GROUP BY a.albumId ORDER BY COUNT(acc) DESC,a.albumId ASC ")
  Page<Album> findAlbumsOrderByCommentCount(Pageable pageable);

  @Query("SELECT a FROM Album a LEFT JOIN a.albumLike al GROUP BY a.albumId ORDER BY COUNT(al.user) DESC,a.albumId ASC")
  Page<Album> findAlbumsOrderByAlbumLikeCount(Pageable pageable);

  Optional<Album> findBySpotifyAlbumId(String spotifyAlbumId);
}
