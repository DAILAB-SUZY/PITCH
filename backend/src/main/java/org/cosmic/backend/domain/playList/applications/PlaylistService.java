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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>플레이리스트 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.</p>
 *
 * <p>사용자의 플레이리스트를 저장, 조회하며, 아티스트 및 트랙 검색 기능을 제공합니다.</p>
 *
 */
@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UsersRepository usersRepository;
    private final TrackRepository trackRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final ArtistRepository artistRepository;

    /**
     * <p>PlaylistService의 생성자입니다.</p>
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
     * <p>사용자의 플레이리스트를 저장 또는 수정합니다.</p>
     *
     * @param userId 사용자 ID
     * @param tracks 새롭게 저장할 플레이리스트 세부 정보 목록
     * @return 저장된 플레이리스트 데이터를 포함한 리스트
     *
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     * @throws NotFoundTrackException 플레이리스트에 포함된 트랙이 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<PlaylistDetail> save(long userId, PlaylistDto tracks) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        List<Long> trackList = tracks.getTrackId();
        Playlist playList = playlistRepository.findByUser_UserId(userId).get();
        playList.setUpdateTime(Instant.now());
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
     * <p>사용자 ID로 플레이리스트를 조회합니다.</p>
     *
     * @param userId 사용자 ID
     * @return 플레이리스트 데이터를 포함한 리스트
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
     * <p>팔로워들의 최신 플레이리스트를 조회합니다.</p>
     *
     * @param userId 사용자 ID
     * @return 팔로워들의 최신 플레이리스트 데이터를 포함한 리스트
     *
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<FollowerPlaylistDetail> followDataOpen(Long userId) {//해당 사용자와 follower인 사람들꺼
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        List<FollowerPlaylistDetail> followerPlaylistDetails=new ArrayList<>();

        //follower레포에서 일단 follower들을 모두 찾겠지 그 뒤에 user로 플레이리스트 찾는것을 하게될거야.

        List<Long>followerIds=new ArrayList<>();

        followerIds.add(1L);
        followerIds.add(2L);
        followerIds.add(3L);
        followerIds.add(4L);
        followerIds.add(5L);

        Pageable pageable = PageRequest.of(0, 5); // 페이징 처리: 5개의 플레이리스트만 가져옴

        followerPlaylistDetails = playlistRepository.findRecentPlaylistsByFollowers(followerIds, pageable)
            .stream()
            .map(FollowerPlaylistDetail::new)
            .collect(Collectors.toList());

        for (int i = 0; i < followerPlaylistDetails.size(); i++) {
            // Playlist ID로 해당 플레이리스트에 속한 트랙을 가져옴
            List<Playlist_Track> playlistTracks = playlistTrackRepository.findByPlaylist_PlaylistId(followerPlaylistDetails.get(i).getPlaylistId())
                    .orElse(Collections.emptyList());

            List<String> trackCover = new ArrayList<>();

            // 각 트랙의 앨범 커버를 리스트에 추가
            for (Playlist_Track playlistTrack : playlistTracks) {
                trackCover.add(playlistTrack.getTrack().getTrackCover());
            }

            // 트랙 커버를 FollowerPlaylistDetail에 설정
            followerPlaylistDetails.get(i).setAlbumCover(trackCover);
        }
        return followerPlaylistDetails;
    }

    /**
     * <p>아티스트 이름으로 트랙을 검색합니다.</p>
     *
     * @param artistName 검색할 아티스트 이름
     * @return 해당 아티스트의 트랙 데이터를 포함한 리스트
     *
     * @throws NotFoundArtistException 아티스트가 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<TrackDetail> artistSearch(String artistName) {
        if (artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return trackRepository.findByArtist_ArtistName(artistName)
                .stream().map(Track::toTrackDetail).toList();
    }

    /**
     * <p>트랙 제목으로 트랙을 검색합니다.</p>
     *
     * @param track 검색할 트랙 제목
     * @return 해당 트랙 데이터를 포함한 리스트
     *
     * @throws NotFoundTrackException 트랙이 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<TrackDetail> trackSearch(String track) {
        if (trackRepository.findByTitle(track).isEmpty()) {
            throw new NotFoundTrackException();
        }
        return List.of(Track.toTrackDetail(trackRepository.findByTitle(track).get()));
    }
}
