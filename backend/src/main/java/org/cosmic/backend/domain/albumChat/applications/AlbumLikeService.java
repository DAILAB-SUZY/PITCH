package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDetail;
import org.cosmic.backend.domain.albumChat.exceptions.ExistAlbumLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumLikeRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.entities.PostLike;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.post.exceptions.NotFoundLikeException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>AlbumLikeService 클래스는 앨범 챗의 좋아요 기능과 관련된 비즈니스 로직을 처리합니다.</p>
 *
 * <p>이 서비스는 앨범에 대한 좋아요의 생성, 조회, 삭제 기능을 제공합니다.</p>
 *
 */
@Service
public class AlbumLikeService {

    private final AlbumLikeRepository likeRepository;
    private final UsersRepository usersRepository;
    private final AlbumRepository albumRepository;

    /**
     * <p>AlbumLikeService 생성자입니다.</p>
     *
     * @param likeRepository 앨범 좋아요 리포지토리
     * @param usersRepository 사용자 리포지토리
     * @param albumRepository 앨범 리포지토리
     */
    public AlbumLikeService(AlbumLikeRepository likeRepository, UsersRepository usersRepository, AlbumRepository albumRepository) {
        this.likeRepository = likeRepository;
        this.usersRepository = usersRepository;
        this.albumRepository = albumRepository;
    }

    /**
     * <p>특정 앨범에 대한 좋아요 목록을 조회합니다.</p>
     *
     * @param albumId 조회할 앨범의 ID
     * @return 좋아요 목록을 포함한 리스트
     *
     * @throws NotFoundAlbumChatException 앨범을 찾을 수 없을 때 발생합니다.
     */
    public List<AlbumChatAlbumLikeDetail> getAlbumChatAlbumLikeByAlbumChatId(Long albumId) {
        if (albumRepository.findById(albumId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        List<AlbumChatAlbumLikeDetail> test=likeRepository.findByAlbum_AlbumId(albumId)
                .stream()
                .map(AlbumChatAlbumLikeDetail::new)
                .collect(Collectors.toList());
        System.out.println(test);
        return test;
    }

    @Transactional
    public List<AlbumChatAlbumLikeDetail> likeOrUnlikeAlbum(Long albumId, Long userId) {
        if (likeRepository.existsByAlbum_AlbumIdAndUser_UserId(albumId, userId)) {
            likeRepository.deleteByAlbum_AlbumIdAndUser_UserId(albumId, userId);
        }
        else{
            likeRepository.save(AlbumLike.builder()
                    .album(albumRepository.findById(albumId).orElseThrow(NotFoundAlbumException::new))
                    .user(usersRepository.findById(userId).orElseThrow(NotFoundUserException::new))
                    .updateTime(Instant.now())
                    .build());
        }
        likeRepository.flush();
        return likeRepository.findByAlbum_AlbumId(albumId)
                .stream()
                .map(AlbumChatAlbumLikeDetail::new)
                .collect(Collectors.toList());
    }
}
