package org.cosmic.backend.domain.bestAlbum.service;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.bestAlbum.domain.Album_User;
import org.cosmic.backend.domain.bestAlbum.dto.AlbumGiveDto;
import org.cosmic.backend.domain.bestAlbum.dto.BestAlbumGiveDto;
import org.cosmic.backend.domain.bestAlbum.exception.NotMatchAlbumException;
import org.cosmic.backend.domain.bestAlbum.repository.Album_UserRepository;
import org.cosmic.backend.domain.playList.domain.*;
import org.cosmic.backend.domain.bestAlbum.dto.*;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repository.*;
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
    public void save(long userId, List<bestAlbumDetail> bestalbumList) {
        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }

        else{
            List<Album_User> albumUsers=album_userRepository.findByUser_UserId(userId).get();
            album_userRepository.deleteByUser_UserId(userId);
            for (bestAlbumDetail bestalbumDetail : bestalbumList) {
                Album album= albumRepository.findByAlbumId(bestalbumDetail.getAlbumId());
                Album_User album_User=new Album_User();
                album_User.setAlbum(album);
                album_User.setUser(usersRepository.findById(userId).get());
                album_userRepository.save(album_User);
            }
        }
    }
    //플레이리스트 생성 또는 수정시 저장

    @Transactional
    public void add(long userId, Long albumId) {

        if(!usersRepository.findById(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else{
            //해당 앨범이 있는건지도 확인필요
            User newuser = usersRepository.findById(userId).get();
            Album album=albumRepository.findById(albumId).get();
            Album_User albumUser=new Album_User(album,newuser);
            album_userRepository.save(albumUser);
        }
    }

    @Transactional
    public List<BestAlbumGiveDto> open(Long userId) {

        List<BestAlbumGiveDto> bestAlbumGiveDtos=new ArrayList<>();

        User newuser=usersRepository.findById(userId).get();

        if(newuser==null)
        {
            throw new NotFoundUserException();//해당 아티스트가 없을 때
        }
        else{
            //Album_User newalbum_user=newuser.getAlbum_users();
            List<Album_User> album_user=album_userRepository.findByUser_UserId(userId).get();//해당 유저의 모든 album을 가져올 것임

            for(int i=0;i<album_user.size();i++)
            {
                BestAlbumGiveDto newbestAlbumGiveDto=new BestAlbumGiveDto();

                newbestAlbumGiveDto.setAlbumId(album_user.get(i).getAlbum().getAlbumId());

                bestAlbumGiveDtos.add(newbestAlbumGiveDto);
            }

            return bestAlbumGiveDtos;
        }
    }


    @Transactional
    public List<AlbumGiveDto> searchArtist (String artist) {
        List<AlbumGiveDto>albumGiveDtos=new ArrayList<>();
        Artist artistInfo= artistRepository.findByArtistName(artist);

        if(artistInfo!=null)
        {
            List<Album> album=albumRepository.findByArtist_ArtistId(artistInfo.getArtistId());//트랙들을 모두 가져옴
            for(int i=0;i<album.size();i++)
            {
                AlbumGiveDto albumGiveDto=new AlbumGiveDto();
                albumGiveDto.setArtistName(artist);
                albumGiveDto.setTitle(album.get(i).getTitle());
                albumGiveDto.setCover("base");
                albumGiveDtos.add(albumGiveDto);
            }
            return albumGiveDtos;
        }
        else{
            throw new NotFoundArtistException();//해당 아티스트가 없을 때
        }
    }

    @Transactional
    public List<AlbumGiveDto> searchAlbum (String album) {
        List<AlbumGiveDto>albumGiveDtos=new ArrayList<>();
        Album albumInfo= albumRepository.findByTitle(album);

        if(albumInfo!=null)
        {
            AlbumGiveDto albumGiveDto=new AlbumGiveDto();
            albumGiveDto.setArtistName(albumInfo.getArtist().getArtistName());
            albumGiveDto.setTitle(albumInfo.getTitle());
            albumGiveDto.setCover("base");
            albumGiveDtos.add(albumGiveDto);
            return albumGiveDtos;
        }
        else{
            throw new NotMatchAlbumException();//해당 곡이 없을 때
            //오류발생
        }
    }
}
