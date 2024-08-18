package org.cosmic.backend.domain.bestAlbum.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.bestAlbum.domains.AlbumUser;
import org.cosmic.backend.domain.bestAlbum.dtos.AlbumGiveDto;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumGiveDto;
import org.cosmic.backend.domain.bestAlbum.exceptions.ExistBestAlbumException;
import org.cosmic.backend.domain.bestAlbum.exceptions.NotMatchBestAlbumException;
import org.cosmic.backend.domain.bestAlbum.repositorys.AlbumUserRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BestAlbumService {
    private final UsersRepository usersRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumUserRepository albumUserRepository;

    public BestAlbumService(UsersRepository usersRepository, AlbumRepository albumRepository, ArtistRepository artistRepository, AlbumUserRepository albumUserRepository) {
        this.usersRepository = usersRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.albumUserRepository = albumUserRepository;
    }

    @Transactional
    public List<BestAlbumGiveDto> open(Long userId) {

        List<BestAlbumGiveDto> bestAlbumGiveDtos=new ArrayList<>();
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        User newuser = usersRepository.findById(userId).get();
        List<AlbumUser> album_user = albumUserRepository.findByUser_UserId(userId).get();

        for (int i = 0; i < album_user.size(); i++) {
            BestAlbumGiveDto newbestAlbumGiveDto = new BestAlbumGiveDto(album_user.get(i).getAlbum().getAlbumId(),
                album_user.get(i).getAlbum().getTitle(),album_user.get(i).getAlbum().getCover());
            bestAlbumGiveDtos.add(newbestAlbumGiveDto);
        }
        return bestAlbumGiveDtos;
    }

    @Transactional
    public void add(long userId, Long albumId) {
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(albumRepository.findById(albumId).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        if(albumUserRepository.findByAlbum_AlbumIdAndUser_UserId(albumId,userId).isPresent()) {
            throw new ExistBestAlbumException();
        }
        User newuser = usersRepository.findById(userId).get();
        Album album=albumRepository.findById(albumId).get();
        AlbumUser albumUser=new AlbumUser(album,newuser);
        albumUserRepository.save(albumUser);
    }

    @Transactional
    public void save(long userId, List<BestAlbumDetail> bestalbumList) {
        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        List<AlbumUser> albumUsers=albumUserRepository.findByUser_UserId(userId).get();
        albumUserRepository.deleteByUser_UserId(userId);

        Set<Long> albumUserIds = new HashSet<>();
        for (AlbumUser albumUser : albumUsers) {
            albumUserIds.add(albumUser.getAlbum().getAlbumId());
        }
        for(int i=0;i<bestalbumList.size();i++)
        {
            if(albumRepository.findById(bestalbumList.get(i).getAlbumId()).isEmpty())
            {
                throw new NotFoundAlbumException();
            }
            if(!albumUserIds.contains(bestalbumList.get(i).getAlbumId()))
            {
                throw new NotMatchBestAlbumException();
            }
            Album album= albumRepository.findById(bestalbumList.get(i).getAlbumId()).get();
            AlbumUser album_User=new AlbumUser();
            album_User.setAlbum(album);
            album_User.setUser(usersRepository.findById(userId).get());
            albumUserRepository.save(album_User);
        }

    }
    @Transactional
    public List<AlbumGiveDto> searchArtist (String artist) {//해당 아티스트가 가지고 있는 모든 앨범들의 정보를 줌
        List<AlbumGiveDto>albumGiveDtos=new ArrayList<>();

        if(artistRepository.findByArtistName(artist).isEmpty()) {
            throw new NotFoundArtistException();
        }
        Artist artistInfo= artistRepository.findByArtistName(artist).get();
        List<Album> album=albumRepository.findAllByArtist_ArtistId(artistInfo.getArtistId());//트랙들을 모두 가져옴
        for(int i=0;i<album.size();i++)
        {
            AlbumGiveDto albumGiveDto=new AlbumGiveDto(album.get(i).getTitle(),artist,album.get(i).getCover());
            albumGiveDtos.add(albumGiveDto);
        }
        return albumGiveDtos;
    }

    @Transactional
    public List<AlbumGiveDto> searchAlbum (String album) {//해당 앨범이름을 가진 모든 앨범들의 정보를 줌
        List<AlbumGiveDto>albumGiveDtos=new ArrayList<>();
        if(albumRepository.findAllByTitle(album).get().isEmpty()) {
            throw new NotFoundAlbumException();
        }
        List<Album> albumInfo= albumRepository.findAllByTitle(album).get();
        for(Album albumInfo1:albumInfo){
            AlbumGiveDto albumGiveDto=new AlbumGiveDto(
                albumInfo1.getTitle(),albumInfo1.getArtist().getArtistName(),albumInfo1.getCover());
            albumGiveDtos.add(albumGiveDto);
        }
        return albumGiveDtos;
    }
}
