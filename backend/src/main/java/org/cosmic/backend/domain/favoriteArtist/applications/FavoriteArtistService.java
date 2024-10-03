package org.cosmic.backend.domain.favoriteArtist.applications;

import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.dtos.*;
import org.cosmic.backend.domain.favoriteArtist.repositorys.FavoriteArtistRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;


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
        if (usersRepository.findById(userId).isEmpty() || favoriteArtistRepository.findByUser_UserId(userId).isEmpty()) {
            throw new NotFoundUserException();
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
    public FavoriteArtistDetail favoriteArtistSaveData(FavoriteRequest favoriteArtist, Long userId) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if (trackRepository.findByTrackIdAndArtist_ArtistId(favoriteArtist.getTrackId(), favoriteArtist.getArtistId()).isEmpty()) {
            throw new NotFoundTrackException();
        }
        if (albumRepository.findByAlbumIdAndArtist_ArtistId(favoriteArtist.getAlbumId(), favoriteArtist.getArtistId()).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        User user = usersRepository.findByUserId(userId).orElseThrow();
        favoriteArtistRepository.deleteByUser_UserId(user.getUserId());
        favoriteArtistRepository.save(FavoriteArtist.builder()
                .artist(artistRepository.findById(favoriteArtist.getArtistId()).orElseThrow())
                .track(trackRepository.findById(favoriteArtist.getTrackId()).orElseThrow())
                .album(albumRepository.findById(favoriteArtist.getAlbumId()).orElseThrow())
                .user(user)
                .build());

        return FavoriteArtist.toFavoriteArtistDto(favoriteArtistRepository.findByUser_UserId(userId).get());
    }
}
