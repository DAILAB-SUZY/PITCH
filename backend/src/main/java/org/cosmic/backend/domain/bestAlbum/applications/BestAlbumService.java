package org.cosmic.backend.domain.bestAlbum.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;
import org.cosmic.backend.domain.bestAlbum.dtos.AlbumInfoDetail;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.bestAlbum.exceptions.ExistBestAlbumException;
import org.cosmic.backend.domain.bestAlbum.exceptions.NotMatchBestAlbumException;
import org.cosmic.backend.domain.bestAlbum.repositorys.UserBestAlbumRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BestAlbumService는 사용자가 가장 좋아하는 앨범을 관리하는 서비스입니다.
 *
 * 이 서비스는 사용자의 좋아요 앨범 목록을 조회, 추가, 저장하고, 아티스트 또는 앨범을 검색하는 기능을 제공합니다.
 *
 */
@Service
public class BestAlbumService {
    private final UsersRepository usersRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final UserBestAlbumRepository userBestAlbumRepository;

    public BestAlbumService(UsersRepository usersRepository, AlbumRepository albumRepository, ArtistRepository artistRepository, UserBestAlbumRepository userBestAlbumRepository) {
        this.usersRepository = usersRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.userBestAlbumRepository = userBestAlbumRepository;
    }

    /**
     * 사용자가 좋아요 한 앨범 목록을 반환합니다.
     *
     * @param userId 사용자의 ID
     * @return 사용자가 좋아요 한 앨범 목록
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     */
    @Transactional
    public List<BestAlbumDetail> open(Long userId) {
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        return userBestAlbumRepository.findByUser_UserId(userId)
                .orElse(Collections.emptyList())
                .stream()
                .map(BestAlbumDetail::new)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 좋아요 앨범 목록에 새 앨범을 추가합니다.
     *
     * @param userId 사용자의 ID
     * @param albumId 추가할 앨범의 ID
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     * @throws ExistBestAlbumException 이미 사용자의 좋아요 목록에 해당 앨범이 존재하는 경우 발생합니다.
     */
    @Transactional
    public List<BestAlbumDetail> add(long userId, Long albumId) {
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(albumRepository.findById(albumId).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        if(userBestAlbumRepository.findByAlbum_AlbumIdAndUser_UserId(albumId,userId).isPresent()) {
            throw new ExistBestAlbumException();
        }
        User newuser = usersRepository.findById(userId).get();
        Album album=albumRepository.findById(albumId).get();
        UserBestAlbum userBestAlbum =UserBestAlbum.builder().album(album).user(newuser).build();
        userBestAlbumRepository.save(userBestAlbum);

        return userBestAlbumRepository.findByUser_UserId(userId)
                .orElse(Collections.emptyList())
                .stream()
                .map(BestAlbumDetail::new)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 좋아요 앨범 목록을 업데이트합니다.
     * 기존 앨범 목록을 지우고 새 목록을 저장합니다.
     *
     * @param userId 사용자의 ID
     * @param bestAlbumList 새로 저장할 앨범 목록
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     * @throws NotMatchBestAlbumException 사용자의 기존 좋아요 앨범 목록과 일치하지 않는 경우 발생합니다.
     */
    @Transactional
    public List<BestAlbumDetail> save(long userId, List<BestAlbumDetail> bestAlbumList) {
        //TODO NotMatchBestAlbum 예외처리 관련 최적화 필요
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        User user = usersRepository.findById(userId).get();
        List<Long> existingBestAlbumsIds = albumRepository.findAlbumByUserId(userId)
                        .stream()
                        .map(Album::getAlbumId)
                        .toList();
        userBestAlbumRepository.deleteByUser_UserId(userId);
        userBestAlbumRepository.saveAll(bestAlbumList
                .stream()
                .map(bestAlbumDetail -> {
                    if (albumRepository.findById(bestAlbumDetail.getAlbumId()).isEmpty()) {
                        throw new NotFoundAlbumException();
                    }
                    if (!existingBestAlbumsIds.contains(bestAlbumDetail.getAlbumId())) {
                        throw new NotMatchBestAlbumException();
                    }
                    return UserBestAlbum.builder().album(albumRepository.findById(bestAlbumDetail.getAlbumId()).get()).user(user).build();
                })
                .toList()
        );
        return userBestAlbumRepository.findByUser_UserId(userId)
                .orElse(Collections.emptyList())
                .stream()
                .map(BestAlbumDetail::new)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 아티스트 이름으로 모든 앨범 정보를 검색합니다.
     *
     * @param artistName 아티스트의 이름
     * @return 해당 아티스트가 가진 앨범들의 정보
     * @throws NotFoundArtistException 아티스트를 찾을 수 없는 경우 발생합니다.
     */
    @Transactional
    public List<AlbumInfoDetail> searchArtist (String artistName) {//해당 아티스트가 가지고 있는 모든 앨범들의 정보를 줌
        if(artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return albumRepository.findAllByArtist_ArtistName(artistName)
                .stream()
                .map(AlbumInfoDetail::new)
                .toList();//트랙들을 모두 가져옴
    }

    /**
     * 주어진 앨범 제목으로 모든 앨범 정보를 검색합니다.
     *
     * @param albumTitle 앨범의 제목
     * @return 해당 제목을 가진 모든 앨범들의 정보
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     */
    @Transactional
    public List<AlbumInfoDetail> searchAlbum (String albumTitle) {//해당 앨범이름을 가진 모든 앨범들의 정보를 줌
        if(albumRepository.findAllByTitle(albumTitle).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        return albumRepository.findAllByTitle(albumTitle)
                .stream()
                .map(AlbumInfoDetail::new)
                .toList();
    }
}
