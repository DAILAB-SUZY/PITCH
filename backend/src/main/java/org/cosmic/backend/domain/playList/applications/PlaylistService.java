package org.cosmic.backend.domain.playList.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.playList.domains.Playlist_Track;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistGiveDto;
import org.cosmic.backend.domain.playList.dtos.TrackGiveDto;
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

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UsersRepository usersRepository;
    private final TrackRepository trackRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final ArtistRepository artistRepository;

    public PlaylistService(PlaylistRepository playlistRepository, UsersRepository usersRepository, TrackRepository trackRepository, PlaylistTrackRepository playlistTrackRepository, ArtistRepository artistRepository) {
        this.playlistRepository = playlistRepository;
        this.usersRepository = usersRepository;
        this.trackRepository = trackRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.artistRepository = artistRepository;
    }

    @Transactional
    public void save(long userId, List<PlaylistDetail> newPlayList) {
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        Playlist playList = playlistRepository.findByUser_UserId(userId).get();//user의 플레이리스트를 찾음
        playList.setUpdatedDate(Instant.now());
        playlistTrackRepository.deleteByPlaylist_PlaylistId(playList.getPlaylistId());
        playlistTrackRepository.saveAll(newPlayList.stream()
                .map(track -> trackRepository.findById(track.getTrackId()).orElseThrow(NotFoundTrackException::new))
                .map(track -> new Playlist_Track(track, playList))
                .toList());
    }
    //플레이리스트 생성 또는 수정시 저장

    @Transactional
    public List<PlaylistGiveDto> open(Long userId) {
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        return usersRepository.findById(userId).get().getPlaylist().getPlaylist_track().stream().map(Playlist_Track::toGiveDto).toList();
    }

    @Transactional
    public List<TrackGiveDto> artistSearch (String artistName) {
        if(artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return trackRepository.findByArtist_ArtistName(artistName).stream().map(Track::toTrackGiveDto).toList();//트랙들을 모두 가져옴
    }

    @Transactional
    public List<TrackGiveDto> trackSearch (String track) {
        //TODO 여기에서는 equal 보다는 like가 맞지 않나?
        if(trackRepository.findByTitle(track).isEmpty()) {
            throw new NotFoundTrackException();
        }
        return List.of(Track.toTrackGiveDto(trackRepository.findByTitle(track).get()));
    }
}
