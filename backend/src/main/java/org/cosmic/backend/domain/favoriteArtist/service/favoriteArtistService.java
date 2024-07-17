package org.cosmic.backend.domain.favoriteArtist.service;
import org.cosmic.backend.domain.favoriteArtist.domain.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.dto.*;
import org.cosmic.backend.domain.favoriteArtist.repository.FavoriteArtistRepository;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.domain.Track;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.post.exception.NotFoundAlbumException;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class favoriteArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private FavoriteArtistRepository favoriteArtistRepository;


    public favoriteArtist giveFavoriteArtistData(Long userId) {
        // 데이터 받을 때
        favoriteArtist favoriteartist=new favoriteArtist();
        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else{
            FavoriteArtist favoriteArtist1=favoriteArtistRepository.findByUser_UserId(userId).get();
            favoriteartist.setAlbumName(favoriteArtist1.getAlbumName());
            favoriteartist.setArtistName(favoriteArtist1.getArtistName());
            favoriteartist.setTrackName(favoriteArtist1.getTrackName());
            favoriteartist.setCover(favoriteArtist1.getCover());
            return favoriteartist;
        }
    }

    public List<ArtistData> searchArtistData(String artistName) {//artist이름 주면
        List<ArtistData> artistDataList = new ArrayList<>();
        List<Artist> artists= new ArrayList<>();
        if(!artistRepository.findByArtistName(artistName).isPresent())
        {
            throw new NotFoundArtistException();
        }
        else{
            artists=artistRepository.findAllByArtistName(artistName).get();
            for(Artist artist:artists) {
                List<Album> album=albumRepository.findAllByArtist_ArtistId(artist.getArtistId());
                for(Album album1:album) {
                    ArtistData artistData=new ArtistData();
                    artistData.setArtistId(artist.getArtistId());
                    artistData.setArtistName(artist.getArtistName());
                    artistData.setTime(album1.getCreatedDate());//해당 앨범이 처음 나온 시간
                    artistData.setAlbumName(album1.getTitle());//앨범
                    artistDataList.add(artistData);
                }
            }
            return artistDataList;
            //해당 아티스트가 가지고 있는 모든 앨범들을 보여줘야함
        }
    }

    public List<AlbumData> searchAlbumData(Long artistId,String albumName) {
        List<AlbumData> albumDataList = new ArrayList<>();

        Album albums;
        if(!albumRepository.findByTitleAndArtist_ArtistId(albumName,artistId).isPresent())
        {
            throw new NotFoundAlbumException();
        }
        else{
            albums= albumRepository.findByTitleAndArtist_ArtistId(albumName,artistId).get();
            List<Track> track=trackRepository.findByAlbum_AlbumIdAndArtist_ArtistId(albums.getAlbumId(),albums.getArtist().getArtistId()).get();
            for(Track track1:track) {
                AlbumData albumData=new AlbumData();
                albumData.setAlbumId(track1.getAlbum().getAlbumId());
                albumData.setTrackName(track1.getTitle());
                albumDataList.add(albumData);
            }

            return albumDataList;
            //해당 앨범에 있는 모든 노래들을 보여줘야함
        }
    }

    public TrackData searchTrackData(Long albumId,String trackName) {
        if(!trackRepository.findByTitleAndAlbum_AlbumId(trackName,albumId).isPresent())
        {
            throw new NotFoundTrackException();
        }
        else{
            Track track=trackRepository.findByTitleAndAlbum_AlbumId(trackName,albumId).get();
            TrackData trackData=new TrackData();
            trackData.setTrackId(track.getTrackId());
            trackData.setTrackName(track.getTitle());
            return trackData;
        }
    }

    public void saveFavoriteArtistData(favoriteArtistDTO favoriteArtist) {
        FavoriteArtist favoriteArtist1=new FavoriteArtist();
        if(!usersRepository.findById(favoriteArtist.getUserId()).isPresent()) {
            throw new NotFoundUserException();
        }
        else{
        favoriteArtist1.setArtistName(favoriteArtist.getArtistName());
        favoriteArtist1.setAlbumName(favoriteArtist.getAlbumName());
        favoriteArtist1.setTrackName(favoriteArtist.getTrackName());
        favoriteArtist1.setCover(favoriteArtist.getCover());
        favoriteArtist1.setUser(usersRepository.findByUserId(favoriteArtist.getUserId()).get());
        favoriteArtistRepository.save(favoriteArtist1);
        }
    }
}
