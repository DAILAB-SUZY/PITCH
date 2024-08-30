package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeIdResponse;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeResponse;
import org.cosmic.backend.domain.albumChat.exceptions.ExistCommentLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundCommentLikeException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentLikeRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AlbumChatCommentLikeService 클래스는 앨범 챗 댓글에 대한 좋아요 기능을 관리하는 서비스 클래스입니다.
 */
@Service
public class AlbumChatCommentLikeService {
    private final AlbumChatCommentLikeRepository likeRepository;
    private final UsersRepository usersRepository;
    private final AlbumChatCommentRepository albumChatCommentRepository;

    /**
     * AlbumChatCommentLikeService 생성자.
     *
     * @param likeRepository 앨범 챗 댓글 좋아요 리포지토리 주입
     * @param usersRepository 사용자 리포지토리 주입
     * @param albumChatCommentRepository 앨범 챗 댓글 리포지토리 주입
     */
    public AlbumChatCommentLikeService(AlbumChatCommentLikeRepository likeRepository, UsersRepository usersRepository, AlbumChatCommentRepository albumChatCommentRepository) {
        this.likeRepository = likeRepository;
        this.usersRepository = usersRepository;
        this.albumChatCommentRepository = albumChatCommentRepository;
    }

    /**
     * 특정 앨범 챗 댓글 ID로 해당 댓글에 달린 좋아요 목록을 조회하는 메서드.
     *
     * @param albumChatCommentId 조회할 앨범 챗 댓글 ID
     * @return List<AlbumChatCommentLikeResponse> 좋아요 목록 반환
     * @throws NotFoundAlbumChatCommentException 앨범 챗 댓글이 존재하지 않을 경우 발생
     */
    public List<AlbumChatCommentLikeResponse> getAlbumChatCommentLikeByAlbumChatCommentId(Long albumChatCommentId) {
        if(albumChatCommentRepository.findById(albumChatCommentId).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        return likeRepository.findByAlbumChatComment_AlbumChatCommentId(albumChatCommentId)
            .stream()
            .map(AlbumChatCommentLikeResponse::new)
            .collect(Collectors.toList());
    }

    /**
     * 앨범 챗 댓글에 새로운 좋아요를 생성하는 메서드.
     *
     * @param userId 좋아요를 생성하는 사용자 ID
     * @param albumChatCommentId 좋아요를 달 앨범 챗 댓글 ID
     * @return AlbumChatCommentLikeIdResponse 생성된 좋아요 ID를 담은 응답 객체
     * @throws NotFoundAlbumChatCommentException 앨범 챗 댓글이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     * @throws ExistCommentLikeException 이미 해당 사용자가 해당 댓글에 좋아요를 눌렀을 경우 발생
     */
    public AlbumChatCommentLikeIdResponse albumChatCommentLikeCreate(Long userId, Long albumChatCommentId) {
        if(albumChatCommentRepository.findById(albumChatCommentId).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        if(usersRepository.findByUserId(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(likeRepository.findByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(albumChatCommentId, userId).isPresent()) {
            throw new ExistCommentLikeException();
        }
        AlbumChatCommentLike like = new AlbumChatCommentLike(usersRepository.findByUserId(userId).get()
            ,albumChatCommentRepository.findById(albumChatCommentId).get());
        return new AlbumChatCommentLikeIdResponse(likeRepository.save(like).getAlbumChatCommentLikeId());
    }

    /**
     * 특정 좋아요 ID로 좋아요를 삭제하는 메서드.
     *
     * @param likeId 삭제할 좋아요 ID
     * @throws NotFoundCommentLikeException 좋아요가 존재하지 않을 경우 발생
     */
    public void albumChatCommentLikeDelete(Long likeId) {
        if(likeRepository.findById(likeId).isEmpty()) {
            throw new NotFoundCommentLikeException();
        }
        likeRepository.deleteById(likeId);
    }
}
