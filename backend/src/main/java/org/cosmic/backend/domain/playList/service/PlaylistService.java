package org.cosmic.backend.domain.playList.service;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.domain.Playlist;
import org.cosmic.backend.domain.playList.domain.Playlist_Track;
import org.cosmic.backend.domain.playList.domain.Track;
import org.cosmic.backend.domain.playList.dto.PlaylistDetail;
import org.cosmic.backend.domain.playList.dto.PlaylistGiveDto;
import org.cosmic.backend.domain.playList.dto.TrackGiveDto;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repository.*;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private PlaylistTrackRepository playlistTrackRepository;
    @Autowired
    private ArtistRepository artistRepository;

    @Transactional
    public void save(long userId, List<PlaylistDetail> playlist) {
        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else{
            List<Playlist_Track> playlistTrack = usersRepository.findByUserId(userId).get().getPlaylist().getPlaylist_track();

            User user = usersRepository.findByUserId(userId).get();

            Playlist newPlaylist = playlistRepository.findByuser(user);//user의 플레이리스트를 찾음
            newPlaylist.setUpdatedDate(Instant.now());
            playlistTrackRepository.deleteByPlaylist_PlaylistId(playlistRepository.findByuser(user).getPlaylistId());

            for (PlaylistDetail playlistdetail : playlist) {
                if(!trackRepository.findById(playlistdetail.getTrackId()).isPresent()) {
                    throw new NotFoundTrackException();
                }
                else{
                    Track track= trackRepository.findBytrackId(playlistdetail.getTrackId());
                    Playlist_Track playlist_track=new Playlist_Track();
                    playlist_track.setTrack(track);
                    playlist_track.setPlaylist(newPlaylist);
                    playlistTrackRepository.save(playlist_track);
                }
            }
        }
    }
    //플레이리스트 생성 또는 수정시 저장

    @Transactional
    public List<PlaylistGiveDto> open(Long userId) {

        List<PlaylistGiveDto> playlistGiveDtos=new ArrayList<>();
        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else{
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
        }


    @Transactional
    public List<TrackGiveDto> searchArtist (String artist) {
        List<TrackGiveDto>trackGiveDtos=new ArrayList<>();
        if(!artistRepository.findByArtistName(artist).isPresent())
        {
            throw new NotFoundArtistException();
        }
        else{
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
    }

    @Transactional
    public List<TrackGiveDto> searchTrack (String track) {
        List<TrackGiveDto>trackGiveDtos=new ArrayList<>();
        if(!trackRepository.findByTitle(track).isPresent())
        {
            throw new NotFoundTrackException();
        }
        else{
            Track trackInfo= trackRepository.findByTitle(track).get();

            TrackGiveDto trackGiveDto=new TrackGiveDto();
            trackGiveDto.setArtistName(trackInfo.getArtist().getArtistName());
            trackGiveDto.setTitle(trackInfo.getTitle());
            trackGiveDto.setCover("base");
            trackGiveDtos.add(trackGiveDto);
            return trackGiveDtos;
        }
    }
}
