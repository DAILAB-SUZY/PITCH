package org.cosmic.backend.domain.albumScore.applications;

import org.cosmic.backend.domain.albumScore.domains.AlbumScore;
import org.cosmic.backend.domain.albumScore.repositorys.AlbumScoreRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumScoreService
{
    private final AlbumScoreRepository albumScoreRepository;
    private final UsersRepository usersRepository;
    private final AlbumRepository albumRepository;

    public AlbumScoreService(AlbumScoreRepository albumScoreRepository, UsersRepository usersRepository, AlbumRepository albumRepository) {
        this.albumScoreRepository = albumScoreRepository;
        this.usersRepository = usersRepository;
        this.albumRepository = albumRepository;
    }

    public AlbumDetail addScore(Long albumId,Long userId,Integer score){
        Album album=albumRepository.findById(albumId).orElseThrow(NotFoundAlbumException::new);
        createAlbumScore(album,userId,score);
        return AlbumDetail.from(AlbumScore.from(usersRepository.findByUserId(userId).get(),album, score));
    }

    public List<AlbumDetail> getScore(Long userId){
        //해당 유저의 모든 점수 매긴 앨범들 가져오기.
        usersRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        return AlbumDetail.fromByAlbumScore(albumScoreRepository.findByUser_UserId(userId));
    }

    public void createAlbumScore(Album album, Long userId, int score){
        if(albumScoreRepository.existsByAlbum_AlbumIdAndUser_UserId(album.getAlbumId(),userId)){//이미 존재하면
            if(score!=0){
                albumScoreRepository.findByAlbum_AlbumIdAndUser_UserId(album.getAlbumId(),userId).setScore(score);
            }
            else{
                albumScoreRepository.delete(albumScoreRepository.findByAlbum_AlbumIdAndUser_UserId(album.getAlbumId(),userId));
            }
        }
        else{//처음 만드는 상황
            if(score!=0){
                albumScoreRepository.save(AlbumScore.from(usersRepository.findByUserId(userId).get(),album,score));
            }
        }
    }
}
