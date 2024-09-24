package org.cosmic.backend.domain.playList.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.playList.domains.Playlist_Track;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.*;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.playList.repositorys.PlaylistTrackRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * 플레이리스트 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 사용자 플레이리스트의 저장, 조회, 아티스트 및 트랙 검색 기능을 제공합니다.
 */
@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UsersRepository usersRepository;
    private final TrackRepository trackRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final ArtistRepository artistRepository;

    /**
     * PlaylistService의 생성자입니다.
     *
     * @param playlistRepository 플레이리스트 데이터를 처리하는 리포지토리
     * @param usersRepository 사용자 데이터를 처리하는 리포지토리
     * @param trackRepository 트랙 데이터를 처리하는 리포지토리
     * @param playlistTrackRepository 플레이리스트-트랙 연관 데이터를 처리하는 리포지토리
     * @param artistRepository 아티스트 데이터를 처리하는 리포지토리
     */
    public PlaylistService(PlaylistRepository playlistRepository, UsersRepository usersRepository, TrackRepository trackRepository, PlaylistTrackRepository playlistTrackRepository, ArtistRepository artistRepository) {
        this.playlistRepository = playlistRepository;
        this.usersRepository = usersRepository;
        this.trackRepository = trackRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.artistRepository = artistRepository;
    }

    /**
     * 사용자 플레이리스트를 저장 또는 수정합니다.
     *
     * @param userId 사용자 ID
     * @param tracks 새롭게 저장할 플레이리스트 세부 정보 목록
     *
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     * @throws NotFoundTrackException 플레이리스트에 포함된 트랙이 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<PlaylistDetail> save(long userId, PlaylistDto tracks) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        List<Long> trackList=tracks.getTrackId();

        Playlist playList = playlistRepository.findByUser_UserId(userId).get(); // 사용자의 플레이리스트를 찾음
        playList.setUpdate_time(Instant.now());
        playlistTrackRepository.deleteByPlaylist_PlaylistId(playList.getPlaylistId());
        playlistTrackRepository.saveAll(trackList.stream()
                .map(trackId -> trackRepository.findById(trackId)
                        .orElseThrow(NotFoundTrackException::new))
                .map(track -> Playlist_Track.builder().track(track).playlist(playList).build())
                .toList());
        return usersRepository.findById(userId).get().getPlaylist().getPlaylist_track()
            .stream().map(Playlist_Track::toGiveDetail).toList();


    }

    /**
     * 사용자 ID로 플레이리스트를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 플레이리스트 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<PlaylistDetail> open(Long userId) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        return usersRepository.findById(userId).get().getPlaylist().getPlaylist_track()
            .stream().map(Playlist_Track::toGiveDetail).toList();
    }

    /**
     * 아티스트 이름으로 트랙을 검색합니다.
     *
     * @param artistName 검색할 아티스트 이름
     * @return 해당 아티스트의 트랙 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundArtistException 아티스트가 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<TrackDetail> artistSearch(String artistName) {
        if (artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return trackRepository.findByArtist_ArtistName(artistName)
            .stream().map(Track::toTrackDetail).toList(); // 트랙들을 모두 가져옴
    }

    /**
     * 트랙 제목으로 트랙을 검색합니다.
     *
     * @param track 검색할 트랙 제목
     * @return 해당 트랙 데이터를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundTrackException 트랙이 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<TrackDetail> trackSearch(String track) {
        // TODO 여기에서는 equal 보다는 like가 맞지 않나?
        if (trackRepository.findByTitle(track).isEmpty()) {
            throw new NotFoundTrackException();
        }
        return List.of(Track.toTrackDetail(trackRepository.findByTitle(track).get()));
    }
}
