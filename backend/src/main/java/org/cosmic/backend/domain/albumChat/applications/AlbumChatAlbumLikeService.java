package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.domains.AlbumChat;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatAlbumLike;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeReq;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeResponse;
import org.cosmic.backend.domain.albumChat.exceptions.ExistAlbumLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatAlbumLikeRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundLikeException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
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
        if(albumChatRepository.findById(albumChatId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        List<AlbumChatAlbumLike> likeList=likeRepository.findByAlbumChat_AlbumChatId(albumChatId);
        for(AlbumChatAlbumLike like:likeList) {
            AlbumChatAlbumLikeResponse likeresponse = new AlbumChatAlbumLikeResponse(
                like.getUser().getUserId(),like.getUser().getUsername(),like.getUser().getProfilePicture()
            );
            likes.add(likeresponse);
        }
        return likes;
    }

    public AlbumChatAlbumLikeReq albumChatAlbumLikeCreate(Long userId, Long albumChatId) {
        if(albumChatRepository.findById(albumChatId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        if(usersRepository.findByUserId(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(likeRepository.findByAlbumChat_AlbumChatIdAndUser_UserId(albumChatId, userId).isPresent())
        {
            throw new ExistAlbumLikeException();
        }
        User user=usersRepository.findByUserId(userId).get();
        AlbumChat albumChat=albumChatRepository.findById(albumChatId).get();
        AlbumChatAlbumLike like = new AlbumChatAlbumLike(user,albumChat);
        likeRepository.save(like);
        AlbumChatAlbumLikeReq likeReq = new AlbumChatAlbumLikeReq(like.getAlbumChatAlbumLikeId());
        return likeReq;
    }

    public void albumChatAlbumLikeDelete(Long likeId) {
        if(likeRepository.findById(likeId).isEmpty()) {
            throw new NotFoundLikeException();
        }
        likeRepository.deleteById(likeId);
    }
}