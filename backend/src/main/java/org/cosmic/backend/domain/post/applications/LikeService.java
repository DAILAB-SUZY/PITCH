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

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UsersRepository usersRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UsersRepository usersRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.usersRepository = usersRepository;
        this.postRepository = postRepository;
    }

    public List<LikeResponse> getLikesByPostId(Long postId) {
        //postId를 통해 이 id의 좋아요한 사람의 정보 다 가져옴
        if(postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        return likeRepository.findByPost_PostId(postId)
                .stream()
                .map(Like::toLikeResponse)
                .toList();
    }

    public LikeReq createLike(Long userId, Long postId) {
        if(postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(likeRepository.findByPost_PostIdAndUser_UserId(postId, userId).isPresent()) {
            throw new ExistLikeException();//409
        }
        return Like.toLikeReq(likeRepository.save(Like.builder()
                .user(usersRepository.findById(userId).get())
                .post(postRepository.findById(postId).get())
                .build()));
    }

    public void deleteLike(Long likeId) {
        if(likeRepository.findById(likeId).isEmpty()) {
            throw new NotFoundLikeException();
        }
        likeRepository.deleteById(likeId);
    }
}