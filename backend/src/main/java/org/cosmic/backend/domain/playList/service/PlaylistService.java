package org.cosmic.backend.domain.playList.service;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDNA.dto.ListDNA;
import org.cosmic.backend.domain.playList.domain.Playlist;
import org.cosmic.backend.domain.playList.domain.Playlist_Track;
import org.cosmic.backend.domain.playList.domain.Track;
import org.cosmic.backend.domain.playList.dto.playlistDetail;
import org.cosmic.backend.domain.playList.dto.playlistGiveDto;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.PlaylistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.playList.repository.playlistTrackRepository;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private playlistTrackRepository playlistTrackRepository;

    @Transactional
    public void save(long userId, List<playlistDetail> playlist) {

        List<Playlist_Track> playlistTrack =playlistRepository.findByuser(usersRepository.findById(userId).get()).getPlaylist_track();

        User user = usersRepository.findById(userId).get();
        Playlist newPlaylist = playlistRepository.findByuser(user);//user의 플레이리스트를 찾음

        if (!playlistTrackRepository.findById(playlistRepository.findByuser(usersRepository.findById(userId).get()).getPlaylistId()).isPresent()) {//이미있다면
            List<Track> tracks = new ArrayList<>();
            for (playlistDetail playlistdetail : playlist) {
                Track track= trackRepository.findBytrackId(playlistdetail.getTrackId());
                Playlist_Track playlist_track=new Playlist_Track();
                playlist_track.setTrack(track);
                playlist_track.setPlaylist(newPlaylist);
                playlistTrackRepository.save(playlist_track);
            }
        }
        else{//없었다면

            for(int i=0;i<playlist.size();i++)
            {
                playlistDetail playlistdetail = playlist.get(i);
                Track track=trackRepository.findBytrackId(playlistdetail.getTrackId());
                Playlist_Track playlist_track=playlistTrack.get(i);
                playlist_track.setTrack(track);
                playlist_track.setPlaylist(newPlaylist);
                playlistTrackRepository.save(playlist_track);
            }
        }

    }
    //플레이리스트 생성 또는 수정시 저장

    @Transactional
    public List<playlistGiveDto> open(Long userId) {

        List<playlistGiveDto> playlistGiveDtos=new ArrayList<>();
        User newuser=usersRepository.findById(userId).get();

        Playlist newplaylist=newuser.getPlaylist();//플레이리스트를 가져오고
        List<Playlist_Track> playlist_track=playlistTrackRepository.findByPlaylist_PlaylistId(newplaylist.getPlaylistId());//해당 플레이리스트로 playlist_track에 해당 플레이리스트에 있는 모든 id가져오기

        System.out.println(playlist_track.size());
        for(int i=0;i<playlist_track.size();i++)
        {
            playlistGiveDto newplaylistGiveDto=new playlistGiveDto();

            newplaylistGiveDto.setPlaylistId(newplaylist.getPlaylistId());
            newplaylistGiveDto.setUserId(newplaylist.getUser().getUserId());
            newplaylistGiveDto.setTrackId(playlist_track.get(i).getTrack().getTrackId());
            newplaylistGiveDto.setTitle(playlist_track.get(i).getTrack().getTitle());
            newplaylistGiveDto.setCover(playlist_track.get(i).getTrack().getCover());
            newplaylistGiveDto.setArtistName(playlist_track.get(i).getTrack().getArtist().getName());

            playlistGiveDtos.add(newplaylistGiveDto);
        }
        return playlistGiveDtos;
    }
}
