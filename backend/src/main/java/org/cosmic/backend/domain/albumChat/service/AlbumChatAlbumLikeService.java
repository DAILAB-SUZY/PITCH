package org.cosmic.backend.domain.albumChat.service;

import org.cosmic.backend.domain.albumChat.domain.AlbumChatAlbumLike;
import org.cosmic.backend.domain.albumChat.dto.albumlike.AlbumChatAlbumLikeReq;
import org.cosmic.backend.domain.albumChat.dto.albumlike.AlbumChatAlbumLikeResponse;
import org.cosmic.backend.domain.albumChat.exception.ExistAlbumLikeException;
import org.cosmic.backend.domain.albumChat.exception.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatAlbumLikeRepository;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exception.NotFoundLikeException;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumChatAlbumLikeService {

    @Autowired
    private AlbumChatAlbumLikeRepository likeRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AlbumChatRepository albumChatRepository;

    public List<AlbumChatAlbumLikeResponse> getAlbumChatAlbumLikeByAlbumChatId(Long albumChatId) {
        List<AlbumChatAlbumLikeResponse> likes = new ArrayList<>();
        //postId를 통해 이 id의 좋아요한 사람의 정보 다 가져옴
        if(!albumChatRepository.findById(albumChatId).isPresent()) {
            throw new NotFoundAlbumChatException();
        }
        else{
            List<AlbumChatAlbumLike> likeList=likeRepository.findByAlbumChat_AlbumChatId(albumChatId);//해당 포스트의 좋아요들을 모두 볼 수 있음
            for(AlbumChatAlbumLike like:likeList) {
                AlbumChatAlbumLikeResponse likeresponse = new AlbumChatAlbumLikeResponse();
                likeresponse.setUserId(like.getUser().getUserId());
                likeresponse.setUserName(like.getUser().getUsername());
                likeresponse.setProfilePicture(like.getUser().getProfilePicture());
                likes.add(likeresponse);
            }
            return likes;
        }
    }

    public AlbumChatAlbumLikeReq AlbumChatcreateAlbumLike(Long userId, Long albumChatId) {
        AlbumChatAlbumLike like = new AlbumChatAlbumLike();
        if(!albumChatRepository.findById(albumChatId).isPresent()) {
            throw new NotFoundAlbumChatException();
        }
        else if(!usersRepository.findByUserId(userId).isPresent()) {
            throw new NotFoundUserException();
        }
        else if(likeRepository.findByAlbumChat_AlbumChatIdAndUser_UserId(albumChatId, userId).isPresent())
        {
            throw new ExistAlbumLikeException();//409
        }
        else{
            like.setUser(usersRepository.findByUserId(userId).get());
            like.setAlbumChat(albumChatRepository.findById(albumChatId).get());
            likeRepository.save(like);
            AlbumChatAlbumLikeReq likeReq = new AlbumChatAlbumLikeReq();
            likeReq.setAlbumChatAlbumLikeId(like.getAlbumChatAlbumLikeId());
            return likeReq;
        }
    }

    public void deleteAlbumChatAlbumLike(Long likeId) {
        if(!likeRepository.findById(likeId).isPresent()) {
            throw new NotFoundLikeException();
        }
        else{
            likeRepository.deleteById(likeId);
        }
    }
}