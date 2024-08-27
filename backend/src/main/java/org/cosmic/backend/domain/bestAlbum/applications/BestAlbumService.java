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
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        return albumUserRepository.findByUser_UserId(userId)
                .orElse(Collections.emptyList())
                .stream()
                .map(BestAlbumGiveDto::new)
                .collect(Collectors.toList());
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
    public void save(long userId, List<BestAlbumDetail> bestAlbumList) {
        //TODO NotMatchBestAlbum 예외처리 관련 최적화 필요
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        User user = usersRepository.findById(userId).get();
        List<Long> existingBestAlbumsIds = albumRepository.findAlbumByUserId(userId)
                        .stream()
                        .map(Album::getAlbumId)
                        .toList();
        albumUserRepository.deleteByUser_UserId(userId);
        albumUserRepository.saveAll(bestAlbumList
                .stream()
                .map(bestAlbumDetail -> {
                    if (albumRepository.findById(bestAlbumDetail.getAlbumId()).isEmpty()) {
                        throw new NotFoundAlbumException();
                    }
                    if (!existingBestAlbumsIds.contains(bestAlbumDetail.getAlbumId())) {
                        throw new NotMatchBestAlbumException();
                    }
                    return new AlbumUser(albumRepository.findById(bestAlbumDetail.getAlbumId()).get(), user);
                })
                .toList()
        );
    }
    @Transactional
    public List<AlbumGiveDto> searchArtist (String artistName) {//해당 아티스트가 가지고 있는 모든 앨범들의 정보를 줌
        if(artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return albumRepository.findAllByArtist_ArtistName(artistName)
                .stream()
                .map(AlbumGiveDto::new)
                .toList();//트랙들을 모두 가져옴
    }

    @Transactional
    public List<AlbumGiveDto> searchAlbum (String albumTitle) {//해당 앨범이름을 가진 모든 앨범들의 정보를 줌
        if(albumRepository.findAllByTitle(albumTitle).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        return albumRepository.findAllByTitle(albumTitle)
                .stream()
                .map(AlbumGiveDto::new)
                .toList();
    }
}
