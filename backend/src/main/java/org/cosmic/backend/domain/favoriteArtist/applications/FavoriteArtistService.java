package org.cosmic.backend.domain.favoriteArtist.applications;

import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.dtos.*;
import org.cosmic.backend.domain.favoriteArtist.repositorys.FavoriteArtistRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p> FavoriteArtistService는 사용자의 즐겨찾기 아티스트 정보를 관리하는 서비스입니다. </p>
 *
 * <p> 이 서비스는 사용자가 즐겨찾는 아티스트, 앨범, 트랙 정보를 검색하고 저장하는 기능을 제공합니다. </p>
 *
 * @author Cosmic
 * @version 1.0
 * @since 2024
 */
@Service
public class FavoriteArtistService {
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final UsersRepository usersRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;

    @Autowired
    private SearchService searchService;
    /**
     * FavoriteArtistService의 생성자.
     *
     * @param artistRepository 아티스트 관련 데이터베이스 접근 레포지토리
     * @param albumRepository 앨범 관련 데이터베이스 접근 레포지토리
     * @param trackRepository 트랙 관련 데이터베이스 접근 레포지토리
     * @param usersRepository 사용자 관련 데이터베이스 접근 레포지토리
     * @param favoriteArtistRepository 즐겨찾기 아티스트 관련 데이터베이스 접근 레포지토리
     */
    public FavoriteArtistService(ArtistRepository artistRepository, AlbumRepository albumRepository, TrackRepository trackRepository, UsersRepository usersRepository, FavoriteArtistRepository favoriteArtistRepository) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.usersRepository = usersRepository;
        this.favoriteArtistRepository = favoriteArtistRepository;
    }

    /**
     * <p>사용자가 즐겨찾는 아티스트 정보를 반환합니다.</p>
     *
     * @param userId 사용자의 ID
     * @return 사용자가 즐겨찾는 아티스트 정보
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     */
    public FavoriteArtistDetail favoriteArtistGiveData(Long userId) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(favoriteArtistRepository.findByUser_UserId(userId).isEmpty())
        {
            return FavoriteArtistDetail.builder()
                    .artistName("없음")
                    .albumCover("없음")
                    .trackCover("없음")
                    .artistCover("없음")
                    .albumName("없음")
                    .trackName("없음")
                    .build();
        }
        return FavoriteArtist.toFavoriteArtistDto(favoriteArtistRepository.findByUser_UserId(userId).get());
    }

    /**
     * <p>사용자의 즐겨찾기 아티스트 정보를 저장합니다.</p>
     *
     * @param favoriteArtist 사용자의 즐겨찾기 아티스트 정보
     * @param userId 사용자의 ID
     * @return 저장된 즐겨찾기 아티스트 정보
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundTrackException 트랙을 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     */
    @Transactional
    public FavoriteArtistDetail favoriteArtistSaveData(FavoriteRequest favoriteArtist, Long userId) {
        Album album;
        Track track;
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }

        if (albumRepository.findBySpotifyAlbumIdAndArtist_SpotifyArtistId(favoriteArtist.getSpotifyAlbumId(), favoriteArtist.getSpotifyArtistId()).isEmpty()
            && trackRepository.findBySpotifyTrackIdAndArtist_SpotifyArtistId(favoriteArtist.getSpotifyTrackId(), favoriteArtist.getSpotifyArtistId()).isEmpty()){
            album=searchService.findAndSaveAlbumBySpotifyId(favoriteArtist.getSpotifyAlbumId());
            track=searchService.findAndSaveTrackBySpotifyId(favoriteArtist.getSpotifyTrackId());
        }
        else{
            album= albumRepository.findBySpotifyAlbumId(favoriteArtist.getSpotifyAlbumId()).get();
            track= trackRepository.findBySpotifyTrackId(favoriteArtist.getSpotifyTrackId()).get();
        }
        //없으면 검색해서 가져옴.
        User user = usersRepository.findByUserId(userId).orElseThrow();
        favoriteArtistRepository.deleteByUser_UserId(user.getUserId());
        FavoriteArtist favoriteArtist2=favoriteArtistRepository.save(FavoriteArtist.builder()
            .artist(artistRepository.findBySpotifyArtistId(favoriteArtist.getSpotifyArtistId()).orElseThrow())
            .track(track)
            .album(album)
            .user(user)
            .build());

        return FavoriteArtist.toFavoriteArtistDto(favoriteArtist2);
    }
}
