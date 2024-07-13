package org.cosmic.backend.domain.bestAlbum.service;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.bestAlbum.domain.Album_User;
import org.cosmic.backend.domain.bestAlbum.dto.AlbumGiveDto;
import org.cosmic.backend.domain.bestAlbum.dto.BestAlbumGiveDto;
import org.cosmic.backend.domain.bestAlbum.exception.NotMatchBestAlbumException;
import org.cosmic.backend.domain.bestAlbum.repository.Album_UserRepository;
import org.cosmic.backend.domain.playList.domain.*;
import org.cosmic.backend.domain.bestAlbum.dto.*;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repository.*;
import org.cosmic.backend.domain.post.exception.NotFoundAlbumException;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class BestAlbumService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private Album_UserRepository album_userRepository;

    @Transactional
    public List<BestAlbumGiveDto> open(Long userId) {

        List<BestAlbumGiveDto> bestAlbumGiveDtos=new ArrayList<>();
        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else {
            User newuser = usersRepository.findById(userId).get();
            //Album_User newalbum_user=newuser.getAlbum_users();
            List<Album_User> album_user = album_userRepository.findByUser_UserId(userId).get();//해당 유저의 모든 album을 가져올 것임

            for (int i = 0; i < album_user.size(); i++) {
                BestAlbumGiveDto newbestAlbumGiveDto = new BestAlbumGiveDto();
                newbestAlbumGiveDto.setAlbumName(album_user.get(i).getAlbum().getTitle());
                newbestAlbumGiveDto.setCover(album_user.get(i).getAlbum().getCover());
                newbestAlbumGiveDto.setAlbumId(album_user.get(i).getAlbum().getAlbumId());
                bestAlbumGiveDtos.add(newbestAlbumGiveDto);
            }
            return bestAlbumGiveDtos;
        }
    }
    @Transactional
    public void add(long userId, Long albumId) {
        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else if(!albumRepository.findById(albumId).isPresent()) {
            throw new NotFoundAlbumException();
        }
        else{
            User newuser = usersRepository.findById(userId).get();
            Album album=albumRepository.findById(albumId).get();
            Album_User albumUser=new Album_User(album,newuser);
            album_userRepository.save(albumUser);
        }
    }

    @Transactional
    public void save(long userId, List<bestAlbumDetail> bestalbumList) {
        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else{
            List<Album_User> albumUsers=album_userRepository.findByUser_UserId(userId).get();
            //앨범에서 내가 추가하지 않은 앨범이 하나라도 있는지 확인해야함. 있다면 에러
            album_userRepository.deleteByUser_UserId(userId);

            for (bestAlbumDetail bestalbumDetail : bestalbumList) {
                if(!albumRepository.findByAlbumId(bestalbumDetail.getAlbumId()).isPresent())
                {
                    throw new NotFoundAlbumException();//해당 앨범이 없는것일때
                }
                else if(!album_userRepository.findByAlbum_AlbumIdAndUser_UserId(bestalbumDetail.getAlbumId(),userId).isPresent())
                {
                    throw new NotMatchBestAlbumException();
                    //해당 유저가 갖고있지 않은 앨범일 때
                }
                else{
                    Album album= albumRepository.findByAlbumId(bestalbumDetail.getAlbumId()).get();
                    Album_User album_User=new Album_User();
                    album_User.setAlbum(album);
                    album_User.setUser(usersRepository.findById(userId).get());
                    album_userRepository.save(album_User);
                }
            }
        }
    }
    //플레이리스트 생성 또는 수정시 저장


    @Transactional
    public List<AlbumGiveDto> searchArtist (String artist) {//해당 아티스트가 가지고 있는 모든 앨범들의 정보를 줌
        List<AlbumGiveDto>albumGiveDtos=new ArrayList<>();

        if(!artistRepository.findByArtistName(artist).isPresent()) {
            throw new NotFoundArtistException();
        }
        else{
            Artist artistInfo= artistRepository.findByArtistName(artist).get();
            List<Album> album=albumRepository.findByArtist_ArtistId(artistInfo.getArtistId());//트랙들을 모두 가져옴
            for(int i=0;i<album.size();i++)
            {
                AlbumGiveDto albumGiveDto=new AlbumGiveDto();
                albumGiveDto.setArtistName(artist);
                albumGiveDto.setTitle(album.get(i).getTitle());
                albumGiveDto.setCover(album.get(i).getCover());
                albumGiveDtos.add(albumGiveDto);
            }
            return albumGiveDtos;

        }
    }

    @Transactional
    public List<AlbumGiveDto> searchAlbum (String album) {//해당 앨범이름을 가진 모든 앨범들의 정보를 줌
        List<AlbumGiveDto>albumGiveDtos=new ArrayList<>();
        if(!albumRepository.findByTitle(album).isPresent()) {
            throw new NotFoundAlbumException();
        }
        else{
            List<Album> albumInfo= albumRepository.findByTitle(album).get();
            for(Album albumInfo1:albumInfo){
                AlbumGiveDto albumGiveDto=new AlbumGiveDto();
                albumGiveDto.setArtistName(albumInfo1.getArtist().getArtistName());
                albumGiveDto.setTitle(albumInfo1.getTitle());
                albumGiveDto.setCover(albumInfo1.getCover());
                albumGiveDtos.add(albumGiveDto);
            }
            return albumGiveDtos;
        }
    }
}
