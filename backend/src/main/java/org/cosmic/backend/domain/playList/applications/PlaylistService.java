package org.cosmic.backend.domain.playList.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.domains.Artist;
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
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
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
    public void save(long userId, List<PlaylistDetail> playlist) {
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }

        List<Playlist_Track>playlistTrack=usersRepository.findByUserId(userId).get().getPlaylist().getPlaylist_track();

        User user = usersRepository.findByUserId(userId).get();

        Playlist newPlaylist = playlistRepository.findByuser(user);//user의 플레이리스트를 찾음
        newPlaylist.setUpdatedDate(Instant.now());
        playlistTrackRepository.deleteByPlaylist_PlaylistId(playlistRepository.findByuser(user).getPlaylistId());

        for (PlaylistDetail playlistdetail : playlist) {
            if(trackRepository.findById(playlistdetail.getTrackId()).isEmpty()) {
                throw new NotFoundTrackException();
            }
            Track track= trackRepository.findBytrackId(playlistdetail.getTrackId());
            Playlist_Track playlist_track=new Playlist_Track();
            playlist_track.setTrack(track);
            playlist_track.setPlaylist(newPlaylist);
            playlistTrackRepository.save(playlist_track);
        }

    }
    //플레이리스트 생성 또는 수정시 저장

    @Transactional
    public List<PlaylistGiveDto> open(Long userId) {
        List<PlaylistGiveDto> playlistGiveDtos=new ArrayList<>();
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        User newuser=usersRepository.findById(userId).get();

        Playlist newplaylist=newuser.getPlaylist();//플레이리스트를 가져오고
        List<Playlist_Track> playlist_track=newplaylist.getPlaylist_track();
        for(int i=0;i<playlist_track.size();i++)
        {
            PlaylistGiveDto newplaylistGiveDto=new PlaylistGiveDto();

            newplaylistGiveDto.setPlaylistId(newplaylist.getPlaylistId());
            newplaylistGiveDto.setUserId(newplaylist.getUser().getUserId());
            newplaylistGiveDto.setTrackId(playlist_track.get(i).getTrack().getTrackId());
            newplaylistGiveDto.setTitle(playlist_track.get(i).getTrack().getTitle());
            newplaylistGiveDto.setCover(playlist_track.get(i).getTrack().getCover());
            newplaylistGiveDto.setArtistName(playlist_track.get(i).getTrack().getArtist().getArtistName());

            playlistGiveDtos.add(newplaylistGiveDto);
        }
        return playlistGiveDtos;
        }


    @Transactional
    public List<TrackGiveDto> artistSearch (String artist) {
        List<TrackGiveDto>trackGiveDtos=new ArrayList<>();
        if(artistRepository.findByArtistName(artist).isEmpty())
        {
            throw new NotFoundArtistException();
        }
        Artist artistInfo= artistRepository.findByArtistName(artist).get();
        List<Track> track=trackRepository.findByArtist_ArtistId(artistInfo.getArtistId());//트랙들을 모두 가져옴
        for(int i=0;i<track.size();i++)
        {
            TrackGiveDto trackGiveDto=new TrackGiveDto();
            trackGiveDto.setArtistName(artist);
            trackGiveDto.setTitle(track.get(i).getTitle());
            trackGiveDto.setCover("base");
            trackGiveDtos.add(trackGiveDto);
        }
        return trackGiveDtos;
    }

    @Transactional
    public List<TrackGiveDto> trackSearch (String track) {
        List<TrackGiveDto>trackGiveDtos=new ArrayList<>();
        if(trackRepository.findByTitle(track).isEmpty())
        {
            throw new NotFoundTrackException();
        }
        Track trackInfo= trackRepository.findByTitle(track).get();

        TrackGiveDto trackGiveDto=new TrackGiveDto();
        trackGiveDto.setArtistName(trackInfo.getArtist().getArtistName());
        trackGiveDto.setTitle(trackInfo.getTitle());
        trackGiveDto.setCover("base");
        trackGiveDtos.add(trackGiveDto);
        return trackGiveDtos;
    }
}
