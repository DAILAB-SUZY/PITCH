package org.cosmic.backend.domain.albumChat.service;

import org.cosmic.backend.domain.albumChat.domain.AlbumChatCommentLike;
import org.cosmic.backend.domain.albumChat.dto.commentlike.AlbumChatCommentLikeReq;
import org.cosmic.backend.domain.albumChat.dto.commentlike.AlbumChatCommentLikeResponse;
import org.cosmic.backend.domain.albumChat.exception.ExistCommentLikeException;
import org.cosmic.backend.domain.albumChat.exception.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exception.NotFoundCommentLikeException;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatCommentLikeRepository;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumChatCommentLikeService {
    @Autowired
    private AlbumChatCommentLikeRepository likeRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AlbumChatCommentRepository albumChatCommentRepository;

    public List<AlbumChatCommentLikeResponse> getAlbumChatCommentLikeByAlbumChatComment(Long albumChatCommentId) {
        List<AlbumChatCommentLikeResponse> likes = new ArrayList<>();
        //postId를 통해 이 id의 좋아요한 사람의 정보 다 가져옴
        if(!albumChatCommentRepository.findById(albumChatCommentId).isPresent()) {
            throw new NotFoundAlbumChatCommentException();
        }
        else{
            List<AlbumChatCommentLike> likeList=likeRepository.findByAlbumChatComment_AlbumChatCommentId(albumChatCommentId);//해당 포스트의 좋아요들을 모두 볼 수 있음
            for(AlbumChatCommentLike like:likeList) {
                AlbumChatCommentLikeResponse likeresponse = new AlbumChatCommentLikeResponse();
                likeresponse.setUserId(like.getUser().getUserId());
                likeresponse.setUserName(like.getUser().getUsername());
                likeresponse.setProfilePicture(like.getUser().getProfilePicture());
                likes.add(likeresponse);
            }
            return likes;
        }
    }

    public AlbumChatCommentLikeReq albumChatCreateCommentLike(Long userId, Long albumChatCommentId) {
        AlbumChatCommentLike like = new AlbumChatCommentLike();
        if(!albumChatCommentRepository.findById(albumChatCommentId).isPresent()) {
            throw new NotFoundAlbumChatCommentException();
        }
        else if(!usersRepository.findByUserId(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else if(likeRepository.findByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(albumChatCommentId, userId).isPresent())
        {
            throw new ExistCommentLikeException();//409
        }
        else{
            like.setUser(usersRepository.findByUserId(userId).get());
            like.setAlbumChatComment(albumChatCommentRepository.findById(albumChatCommentId).get());
            likeRepository.save(like);
            AlbumChatCommentLikeReq likeReq = new AlbumChatCommentLikeReq();
            likeReq.setAlbumChatCommentLikeId(like.getAlbumChatCommentLikeId());
            return likeReq;
        }
    }

    public void deleteAlbumChatCommentLike(Long likeId) {
        if(!likeRepository.findById(likeId).isPresent()) {
            throw new NotFoundCommentLikeException();
        }
        else{
            likeRepository.deleteById(likeId);
        }
    }
}
