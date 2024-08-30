package org.cosmic.backend.domain.post.applications;

import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dtos.Like.LikeReq;
import org.cosmic.backend.domain.post.dtos.Like.LikeResponse;
import org.cosmic.backend.domain.post.entities.Like;
import org.cosmic.backend.domain.post.exceptions.ExistLikeException;
import org.cosmic.backend.domain.post.exceptions.NotFoundLikeException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.post.repositories.LikeRepository;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 좋아요 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 좋아요 조회, 생성, 삭제 기능을 제공합니다.
 */
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UsersRepository usersRepository;
    private final PostRepository postRepository;

    /**
     * LikeService의 생성자입니다.
     *
     * @param likeRepository 좋아요 데이터를 처리하는 리포지토리
     * @param usersRepository 사용자 데이터를 처리하는 리포지토리
     * @param postRepository 게시글 데이터를 처리하는 리포지토리
     */
    public LikeService(LikeRepository likeRepository, UsersRepository usersRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.usersRepository = usersRepository;
        this.postRepository = postRepository;
    }

    /**
     * 게시글 ID로 좋아요 목록을 조회합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 게시글에 해당하는 좋아요 응답 객체 리스트
     *
     * @throws NotFoundPostException 게시글이 존재하지 않을 경우 발생합니다.
     */
    public List<LikeResponse> getLikesByPostId(Long postId) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        return likeRepository.findByPost_PostId(postId)
                .stream()
                .map(Like::toLikeResponse)
                .toList();
    }

    /**
     * 새로운 좋아요를 생성합니다.
     *
     * @param userId 좋아요를 남긴 사용자의 ID
     * @param postId 좋아요를 남길 게시글의 ID
     * @return 생성된 좋아요 요청 객체
     *
     * @throws NotFoundPostException 게시글이 존재하지 않을 경우 발생합니다.
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     * @throws ExistLikeException 해당 사용자가 이미 게시글에 좋아요를 남겼을 경우 발생합니다.
     */
    public LikeReq createLike(Long userId, Long postId) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if (likeRepository.findByPost_PostIdAndUser_UserId(postId, userId).isPresent()) {
            throw new ExistLikeException(); // 409 Conflict
        }
        return Like.toLikeReq(likeRepository.save(Like.builder()
                .user(usersRepository.findById(userId).get())
                .post(postRepository.findById(postId).get())
                .build()));
    }

    /**
     * 좋아요를 삭제합니다.
     *
     * @param likeId 삭제할 좋아요의 ID
     *
     * @throws NotFoundLikeException 좋아요가 존재하지 않을 경우 발생합니다.
     */
    public void deleteLike(Long likeId) {
        if (likeRepository.findById(likeId).isEmpty()) {
            throw new NotFoundLikeException();
        }
        likeRepository.deleteById(likeId);
    }
}