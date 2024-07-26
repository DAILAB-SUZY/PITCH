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

    public List<AlbumChatCommentLikeResponse> getAlbumChatCommentLikeByAlbumChatCommentId(Long albumChatCommentId) {
        List<AlbumChatCommentLikeResponse> likes = new ArrayList<>();
        if(albumChatCommentRepository.findById(albumChatCommentId).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        List<AlbumChatCommentLike>likeList=likeRepository.findByAlbumChatComment_AlbumChatCommentId(albumChatCommentId);
        for(AlbumChatCommentLike like:likeList) {
            Long userId=like.getUser().getUserId();
            String userName=like.getUser().getUsername();
            String profilePicture=like.getUser().getProfilePicture();
            AlbumChatCommentLikeResponse likeresponse = new AlbumChatCommentLikeResponse(
                userId,userName,profilePicture
            );
            likes.add(likeresponse);
        }
        return likes;
    }

    public AlbumChatCommentLikeIdResponse albumChatCommentLikeCreate(Long userId, Long albumChatCommentId) {
        if(albumChatCommentRepository.findById(albumChatCommentId).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        if(usersRepository.findByUserId(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(likeRepository.findByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(
            albumChatCommentId, userId).isPresent())
        {
            throw new ExistCommentLikeException();
        }
        AlbumChatCommentLike like = new AlbumChatCommentLike(usersRepository.findByUserId(userId).get()
            ,albumChatCommentRepository.findById(albumChatCommentId).get());
        likeRepository.save(like);
        AlbumChatCommentLikeIdResponse likeReq = new AlbumChatCommentLikeIdResponse(like.getAlbumChatCommentLikeId());
        return likeReq;
    }

    public void albumChatCommentLikeDelete(Long likeId) {
        if(likeRepository.findById(likeId).isEmpty()) {
            throw new NotFoundCommentLikeException();
        }
        likeRepository.deleteById(likeId);
    }
}
