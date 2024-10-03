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
 * <p> BestAlbumService는 사용자가 좋아하는 앨범을 관리하는 서비스입니다. </p>
 *
 * <p> 이 서비스는 사용자의 좋아요 앨범 목록을 조회, 추가, 저장하고, 아티스트 또는 앨범을 검색하는 기능을 제공합니다. </p>
 *
 */
@Service
public class BestAlbumService {

    private final UsersRepository usersRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final UserBestAlbumRepository userBestAlbumRepository;

    /**
     * BestAlbumService 생성자.
     *
     * @param usersRepository 유저 관련 데이터베이스 접근 레포지토리
     * @param albumRepository 앨범 관련 데이터베이스 접근 레포지토리
     * @param artistRepository 아티스트 관련 데이터베이스 접근 레포지토리
     * @param userBestAlbumRepository 유저의 좋아요 앨범 관련 데이터베이스 접근 레포지토리
     */
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
     * @param score 추가할 앨범의 점수
     * @param userId 사용자의 ID
     * @param albumId 추가할 앨범의 ID
     * @return 업데이트된 사용자의 좋아요 앨범 목록
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     * @throws ExistBestAlbumException 사용자의 좋아요 목록에 해당 앨범이 이미 존재하는 경우 발생합니다.
     */
    @Transactional
    public List<BestAlbumDetail> add(int score, Long userId, Long albumId) {
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(albumRepository.findById(albumId).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        if(userBestAlbumRepository.findByAlbum_AlbumIdAndUser_UserId(albumId, userId).isPresent()) {
            throw new ExistBestAlbumException();
        }
        User newUser = usersRepository.findById(userId).get();
        Album album = albumRepository.findById(albumId).get();
        UserBestAlbum userBestAlbum = UserBestAlbum.builder()
                .album(album)
                .user(newUser)
                .score(score)
                .build();
        userBestAlbumRepository.save(userBestAlbum);

        return userBestAlbumRepository.findByUser_UserId(userId)
                .orElse(Collections.emptyList())
                .stream()
                .map(BestAlbumDetail::new)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 좋아요 앨범 목록을 업데이트합니다. 기존 목록을 삭제하고 새 목록을 저장합니다.
     *
     * @param userId 사용자의 ID
     * @param bestAlbumList 새로 저장할 앨범 목록
     * @return 업데이트된 사용자의 좋아요 앨범 목록
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     * @throws NotMatchBestAlbumException 사용자의 기존 좋아요 앨범 목록과 일치하지 않는 경우 발생합니다.
     */
    @Transactional
    public List<BestAlbumDetail> save(long userId, List<BestAlbumDetail> bestAlbumList) {
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        User user = usersRepository.findById(userId).get();
        List<Long> existingBestAlbumIds = albumRepository.findAlbumByUserId(userId)
                .stream()
                .map(Album::getAlbumId)
                .toList();
        userBestAlbumRepository.deleteByUser_UserId(userId);
        userBestAlbumRepository.saveAll(bestAlbumList
                .stream()
                .map(bestAlbumDetail -> {
                    if(albumRepository.findById(bestAlbumDetail.getAlbumId()).isEmpty()) {
                        throw new NotFoundAlbumException();
                    }
                    if(!existingBestAlbumIds.contains(bestAlbumDetail.getAlbumId())) {
                        throw new NotMatchBestAlbumException();
                    }
                    return UserBestAlbum.builder()
                            .album(albumRepository.findById(bestAlbumDetail.getAlbumId()).get())
                            .user(user)
                            .build();
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
     * 주어진 아티스트 이름을 기반으로 해당 아티스트의 모든 앨범 정보를 검색합니다.
     *
     * @param artistName 아티스트의 이름
     * @return 해당 아티스트의 앨범 목록
     * @throws NotFoundArtistException 아티스트를 찾을 수 없는 경우 발생합니다.
     */
    @Transactional
    public List<AlbumInfoDetail> searchArtist(String artistName) {
        if(artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return albumRepository.findAllByArtist_ArtistName(artistName)
                .stream()
                .map(AlbumInfoDetail::new)
                .toList();
    }

    /**
     * 주어진 앨범 제목을 기반으로 모든 앨범 정보를 검색합니다.
     *
     * @param albumTitle 앨범의 제목
     * @return 해당 제목을 가진 앨범 목록
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     */
    @Transactional
    public List<AlbumInfoDetail> searchAlbum(String albumTitle) {
        if(albumRepository.findAllByTitle(albumTitle).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        return albumRepository.findAllByTitle(albumTitle)
                .stream()
                .map(AlbumInfoDetail::new)
                .toList();
    }
}
