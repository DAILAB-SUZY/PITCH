package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumLikeReq;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeResponse;
import org.cosmic.backend.domain.albumChat.exceptions.ExistAlbumLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumLikeRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.exceptions.NotFoundLikeException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;
/**
 * AlbumLikeService 클래스는 앨범 챗의 좋아요 기능과 관련된 비즈니스 로직을 처리합니다.
 */
@Service
public class AlbumLikeService {
    private final AlbumLikeRepository likeRepository;
    private final UsersRepository usersRepository;
    private final AlbumRepository albumRepository;
    /**
     * AlbumLikeService 생성자.
     *
     * @param likeRepository AlbumLikeRepository 주입
     * @param usersRepository UsersRepository 주입
     * @param albumRepository AlbumRepository 주입
     */
    public AlbumLikeService(AlbumLikeRepository likeRepository, UsersRepository usersRepository, AlbumRepository albumRepository) {
        this.likeRepository = likeRepository;
        this.usersRepository = usersRepository;
        this.albumRepository = albumRepository;
    }
    /**
     * 주어진 앨범 챗 ID에 해당하는 앨범 챗의 좋아요 목록을 반환합니다.
     *
     * @param albumId 조회할 앨범 Id
     * @return List<AlbumChatAlbumLikeResponse> 앨범 챗의 좋아요 누른 인물 정보 목록
     * @throws NotFoundAlbumChatException 특정 앨범 챗을 찾을 수 없는 경우 발생
     */
    public List<AlbumChatAlbumLikeResponse> getAlbumChatAlbumLikeByAlbumChatId(Long albumId) {
        if(albumRepository.findById(albumId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }

        return likeRepository.findByAlbum_AlbumId(albumId)
                .stream()
                .map(AlbumChatAlbumLikeResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 앨범 챗에 좋아요를 생성합니다.
     *
     * @param userId 좋아요를 남길 사용자의 ID
     * @param albumId 좋아요를 남길 앨범 챗의 ID
     * @return AlbumLikeReq 생성된 좋아요의 ID를 포함한 객체
     * @throws NotFoundAlbumChatException 특정 앨범 챗을 찾을 수 없는 경우 발생
     * @throws NotFoundUserException 특정 사용자를 찾을 수 없는 경우 발생
     * @throws ExistAlbumLikeException 해당 앨범 챗에 이미 좋아요를 남긴 경우 발생
     */
    public AlbumLikeReq albumChatAlbumLikeCreate(Long userId, Long albumId) {
        if(albumRepository.findById(albumId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        if(usersRepository.findByUserId(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(likeRepository.findByAlbum_AlbumIdAndUser_UserId(albumId, userId).isPresent()) {
            throw new ExistAlbumLikeException();
        }
        User user=usersRepository.findByUserId(userId).get();
        Album album= albumRepository.findById(albumId).get();
        AlbumLike like = likeRepository.save(AlbumLike.builder().user(user).album(album).build());

        return AlbumLike.toLikeReq(like);
    }
    /**
     * 주어진 좋아요 ID에 해당하는 좋아요를 삭제합니다.
     *
     * @param albumId 삭제할 좋아요의 ID
     * @throws NotFoundLikeException 특정 좋아요를 찾을 수 없는 경우 발생
     */
    public void albumChatAlbumLikeDelete(Long albumId, Long userId) {
        if(likeRepository.findByAlbum_AlbumIdAndUser_UserId(albumId, userId).isEmpty()) {
            throw new NotFoundLikeException();
        }
        likeRepository.deleteByAlbum_AlbumIdAndUser_UserId(albumId, userId);
    }
}