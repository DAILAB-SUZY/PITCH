package org.cosmic.backend.domain.post.service;

import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dto.Like.LikeDto;
import org.cosmic.backend.domain.post.dto.Like.LikeReq;
import org.cosmic.backend.domain.post.dto.Like.LikeResponse;
import org.cosmic.backend.domain.post.entity.Like;
import org.cosmic.backend.domain.post.exception.ExistLikeException;
import org.cosmic.backend.domain.post.exception.NotFoundLikeException;
import org.cosmic.backend.domain.post.exception.NotFoundPostException;
import org.cosmic.backend.domain.post.repository.LikeRepository;
import org.cosmic.backend.domain.post.repository.PostRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeService {
@Autowired
private LikeRepository likeRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PostRepository postRepository;

    public List<LikeResponse> getLikesByPostId(Long postId) {
        List<LikeResponse> likes = new ArrayList<>();
        //postId를 통해 이 id의 좋아요한 사람의 정보 다 가져옴
        LikeResponse likeresponse = new LikeResponse();
        if(!postRepository.findById(postId).isPresent()) {
            throw new NotFoundPostException();
        }
        else{
            List<Like> likeList=likeRepository.findByPost_PostId(postId);//해당 포스트의 좋아요들을 모두 볼 수 있음
            for(Like like:likeList) {

                likeresponse.setUserId(like.getUser().getUserId());
                likeresponse.setUserName(like.getUser().getUsername());
                likeresponse.setProfilePicture(like.getUser().getProfilePicture());
                likes.add(likeresponse);
            }
            return likes;
        }
    }

    public LikeReq createLike(Long userId, Long postId) {
        Like like = new Like();
        if(!postRepository.findById(postId).isPresent()) {
            throw new NotFoundPostException();
        }
        else if(!usersRepository.findByUserId(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else if(likeRepository.findByPost_PostIdAndUser_UserId(postId, userId).isPresent())
        {
            throw new ExistLikeException();//409
        }
        else{
            like.setUser(usersRepository.findByUserId(userId).get());
            like.setPost(postRepository.findByPostId(postId));
            likeRepository.save(like);
            LikeDto likeDto = new LikeDto();
            LikeReq likeReq = new LikeReq();
            likeReq.setLikeId(like.getLikeId());
            return likeReq;
        }
    }

    public void deleteLike(Long likeId) {
        if(!likeRepository.findById(likeId).isPresent()) {
            throw new NotFoundLikeException();
        }
        else{
        likeRepository.deleteById(likeId);
        }
    }
}