package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.albumChat.exceptions.ExistCommentLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundCommentLikeException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentLikeRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.entities.PostLike;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>AlbumChatCommentLikeService 클래스는 앨범 챗 댓글에 대한 좋아요 기능을 관리하는 서비스 클래스입니다.</p>
 *
 * <p>이 서비스는 댓글에 대한 좋아요의 생성, 조회, 삭제 기능을 제공합니다.</p>
 *
 */
@Service
public class AlbumChatCommentLikeService {

    private final AlbumChatCommentLikeRepository albumChatCommentLikeRepository;
    private final UsersRepository usersRepository;
    private final AlbumChatCommentRepository albumChatCommentRepository;

    /**
     * <p>AlbumChatCommentLikeService 생성자입니다.</p>
     *
     * @param albumChatCommentLikeRepository 앨범 챗 댓글 좋아요 리포지토리
     * @param usersRepository 사용자 리포지토리
     * @param albumChatCommentRepository 앨범 챗 댓글 리포지토리
     */
    public AlbumChatCommentLikeService(AlbumChatCommentLikeRepository albumChatCommentLikeRepository,
                                       UsersRepository usersRepository,
                                       AlbumChatCommentRepository albumChatCommentRepository) {
        this.albumChatCommentLikeRepository = albumChatCommentLikeRepository;
        this.usersRepository = usersRepository;
        this.albumChatCommentRepository = albumChatCommentRepository;
    }

    /**
     * <p>특정 앨범챗 댓글에 대한 좋아요 목록을 조회합니다.</p>
     *
     * @param albumChatCommentId 조회할 댓글 ID
     * @return 좋아요 목록을 포함한 리스트
     *
     * @throws NotFoundAlbumChatCommentException 앨범챗 댓글을 찾을 수 없을 때 발생합니다.
     */
    public List<AlbumChatCommentLikeDetail> getAlbumChatCommentLikeByAlbumChatCommentId(Long albumChatCommentId) {
        if (albumChatCommentRepository.findById(albumChatCommentId).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        return albumChatCommentLikeRepository.findByAlbumChatComment_AlbumChatCommentId(albumChatCommentId)
                .stream()
                .map(AlbumChatCommentLikeDetail::new)
                .collect(Collectors.toList());
    }

    /**
     * <p>특정 앨범챗 댓글에 새로운 좋아요를 생성합니다.</p>
     *
     * @param userId 좋아요를 생성하는 사용자 ID
     * @param albumChatCommentId 좋아요를 추가할 댓글 ID
     * @return 생성된 좋아요 목록을 포함한 리스트
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     * @throws NotFoundAlbumChatCommentException 앨범챗 댓글을 찾을 수 없을 때 발생합니다.
     * @throws ExistCommentLikeException 이미 존재하는 좋아요가 있을 때 발생합니다.
     */
    public List<AlbumChatCommentLikeDetail> albumChatCommentLikeCreate(Long userId, Long albumChatCommentId) {
        if (albumChatCommentRepository.findById(albumChatCommentId).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        if (usersRepository.findByUserId(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if (albumChatCommentLikeRepository.findByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(albumChatCommentId, userId).isPresent()) {
            throw new ExistCommentLikeException();
        }
        albumChatCommentLikeRepository.save(AlbumChatCommentLike.builder()
                .user(usersRepository.findByUserId(userId).get())
                .albumChatComment(albumChatCommentRepository.findById(albumChatCommentId).get())
                .build());

        return albumChatCommentLikeRepository.findByAlbumChatComment_AlbumChatCommentId(albumChatCommentId)
                .stream()
                .map(AlbumChatCommentLikeDetail::new)
                .collect(Collectors.toList());
    }

    /**
     * <p>특정 앨범챗 댓글에 대한 좋아요를 삭제합니다.</p>
     *
     * @param albumChatCommentId 좋아요를 삭제할 댓글 ID
     * @param userId 좋아요를 삭제하는 사용자 ID
     * @return 삭제 후 남은 좋아요 목록을 포함한 리스트
     *
     * @throws NotFoundCommentLikeException 좋아요를 찾을 수 없을 때 발생합니다.
     */
    public List<AlbumChatCommentLikeDetail> albumChatCommentLikeDelete(Long albumChatCommentId, Long userId) {
        if (albumChatCommentLikeRepository.findByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(albumChatCommentId, userId).isEmpty()) {
            throw new NotFoundCommentLikeException();
        }
        albumChatCommentLikeRepository.deleteByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(albumChatCommentId, userId);

        return albumChatCommentLikeRepository.findByAlbumChatComment_AlbumChatCommentId(albumChatCommentId)
                .stream()
                .map(AlbumChatCommentLikeDetail::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AlbumChatCommentLikeDetail> likeOrUnlikeAlbumChat(Long albumChatCommentId, Long userId) {
        if(albumChatCommentLikeRepository.existsByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(albumChatCommentId, userId)){
            albumChatCommentLikeRepository.deleteByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(albumChatCommentId, userId);
        }
        else{
            albumChatCommentLikeRepository.save(AlbumChatCommentLike.builder()
                    .albumChatComment(albumChatCommentRepository.findById(albumChatCommentId).orElseThrow(NotFoundAlbumChatCommentException::new))
                    .user(usersRepository.findById(userId).orElseThrow(NotFoundUserException::new))
                    .updateTime(Instant.now())
                    .build());
        }
        albumChatCommentLikeRepository.flush();
        return albumChatCommentLikeRepository.findByAlbumChatComment_AlbumChatCommentId(albumChatCommentId)
                .stream()
                .map(AlbumChatCommentLikeDetail::new)
                .collect(Collectors.toList());
    }
}
