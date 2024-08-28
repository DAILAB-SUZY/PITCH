package org.cosmic.backend.domain.favoriteArtist.applications;

import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.favoriteArtist.dtos.*;
import org.cosmic.backend.domain.favoriteArtist.repositorys.FavoriteArtistRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FavoriteArtistService {
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final UsersRepository usersRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;

    public FavoriteArtistService(ArtistRepository artistRepository, AlbumRepository albumRepository, TrackRepository trackRepository, UsersRepository usersRepository, FavoriteArtistRepository favoriteArtistRepository) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.usersRepository = usersRepository;
        this.favoriteArtistRepository = favoriteArtistRepository;
    }


    public FavoriteArtistDto favoriteArtistGiveData(Long userId) {
        if(usersRepository.findById(userId).isEmpty()||favoriteArtistRepository.findByUser_UserId(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        return new FavoriteArtistDto(favoriteArtistRepository.findByUser_UserId(userId).get());
    }

    public List<ArtistData> artistSearchData(String artistName) {//artist이름 주면
        if(artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return albumRepository.findAllArtistDataByArtistId(artistName);
    }

    public List<AlbumData> albumSearchData(Long artistId,String albumName) {
        if(albumRepository.findByTitleAndArtist_ArtistId(albumName,artistId).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        return trackRepository.findByAlbum_TitleAndArtist_ArtistId(albumName, artistId)
                .stream()
                .map(AlbumData::new)
                .toList();
    }

    public TrackData trackSearchData(Long albumId,String trackName) {
        if(trackRepository.findByTitleAndAlbum_AlbumId(trackName,albumId).isEmpty()) {
            throw new NotFoundTrackException();
        }
        return new TrackData(trackRepository.findByTitleAndAlbum_AlbumId(trackName,albumId).get());
    }

    public void favoriteArtistSaveData(FavoriteReq favoriteArtist) {
        if(usersRepository.findById(favoriteArtist.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(trackRepository.findByTrackIdAndArtist_ArtistId
                (favoriteArtist.getTrackId(),favoriteArtist.getArtistId()).isEmpty()) {
            throw new NotFoundTrackException();
        }
        if(albumRepository.findByAlbumIdAndArtist_ArtistId
            (favoriteArtist.getAlbumId(),favoriteArtist.getArtistId()).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        User user=usersRepository.findByUserId(favoriteArtist.getUserId()).orElseThrow();
        favoriteArtistRepository.deleteByUser_UserId(user.getUserId());
        favoriteArtistRepository.save(new FavoriteArtist(
                artistRepository.findById(favoriteArtist.getArtistId()).orElseThrow().getArtistName(),
                albumRepository.findById(favoriteArtist.getAlbumId()).orElseThrow().getTitle(),
                trackRepository.findById(favoriteArtist.getTrackId()).orElseThrow().getTitle(),
                favoriteArtist.getCover(),user));
    }
}
