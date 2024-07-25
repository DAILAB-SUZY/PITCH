package org.cosmic.backend.domain.favoriteArtist.applications;

import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.dtos.*;
import org.cosmic.backend.domain.favoriteArtist.repositorys.FavoriteArtistRepository;
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
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class FavoriteArtistService {

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


    public FavoriteArtistDto favoriteArtistGiveData(Long userId) {
        if(usersRepository.findById(userId).isEmpty()||favoriteArtistRepository.findByUser_UserId(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        FavoriteArtist favoriteArtist1=favoriteArtistRepository.findByUser_UserId(userId).get();
        return new FavoriteArtistDto(favoriteArtist1.getAlbumName(),
            favoriteArtist1.getArtistName(),favoriteArtist1.getTrackName(),favoriteArtist1.getCover());
    }

    public List<ArtistData> artistSearchData(String artistName) {//artist이름 주면
        List<ArtistData> artistDataList = new ArrayList<>();
        if(artistRepository.findByArtistName(artistName).isEmpty())
        {
            throw new NotFoundArtistException();
        }
        List<Artist> artists=artistRepository.findAllByArtistName(artistName).get();
        for(Artist artist:artists) {
            List<Album> album=albumRepository.findAllByArtist_ArtistId(artist.getArtistId());
            for(Album album1:album) {
                ArtistData artistData=new ArtistData(artist.getArtistId(),
                    artist.getArtistName(),album1.getCreatedDate(),album1.getTitle());
                artistDataList.add(artistData);
            }
        }
        return artistDataList;
    }

    public List<AlbumData> albumSearchData(Long artistId,String albumName) {
        List<AlbumData> albumDataList = new ArrayList<>();

        Album albums;
        if(albumRepository.findByTitleAndArtist_ArtistId(albumName,artistId).isEmpty())
        {
            throw new NotFoundAlbumException();
        }
        albums= albumRepository.findByTitleAndArtist_ArtistId(albumName,artistId).get();
        List<Track> track=trackRepository.findByAlbum_AlbumIdAndArtist_ArtistId(albums.getAlbumId(),albums.getArtist().getArtistId()).get();
        for(Track track1:track) {
            AlbumData albumData=new AlbumData(track1.getAlbum().getAlbumId(),track1.getTitle());
            albumDataList.add(albumData);
        }
        return albumDataList;
    }

    public TrackData trackSearchData(Long albumId,String trackName) {
        if(trackRepository.findByTitleAndAlbum_AlbumId(trackName,albumId).isEmpty())
        {
            throw new NotFoundTrackException();
        }
        Track track=trackRepository.findByTitleAndAlbum_AlbumId(trackName,albumId).get();
        return new TrackData(track.getTrackId(),track.getTitle());
    }

    public void favoriteArtistSaveData(FavoriteReq favoriteArtist) {
        if(usersRepository.findById(favoriteArtist.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        User user=usersRepository.findByUserId(favoriteArtist.getUserId()).get();
        FavoriteArtist favoriteArtist1=new FavoriteArtist(favoriteArtist.getArtistName(),favoriteArtist.getAlbumName(),
            favoriteArtist.getTrackName(),favoriteArtist.getCover(),user);
        favoriteArtistRepository.save(favoriteArtist1);
    }
}
