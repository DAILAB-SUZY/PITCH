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
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumChatAlbumLikeService {
    private final AlbumChatAlbumLikeRepository likeRepository;
    private final UsersRepository usersRepository;
    private final AlbumChatRepository albumChatRepository;

    public AlbumChatAlbumLikeService(AlbumChatAlbumLikeRepository likeRepository, UsersRepository usersRepository, AlbumChatRepository albumChatRepository) {
        this.likeRepository = likeRepository;
        this.usersRepository = usersRepository;
        this.albumChatRepository = albumChatRepository;
    }

    public List<AlbumChatAlbumLikeResponse> getAlbumChatAlbumLikeByAlbumChatId(Long albumChatId) {
        if(albumChatRepository.findById(albumChatId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }

        return likeRepository.findByAlbumChat_AlbumChatId(albumChatId)
                .stream()
                .map(AlbumChatAlbumLikeResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * @param userId
     * @param albumChatId
     * @return AlbumChatAlbumLikeReq
     */

    public AlbumChatAlbumLikeReq albumChatAlbumLikeCreate(Long userId, Long albumChatId) {
        if(albumChatRepository.findById(albumChatId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        if(usersRepository.findByUserId(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(likeRepository.findByAlbumChat_AlbumChatIdAndUser_UserId(albumChatId, userId).isPresent()) {
            throw new ExistAlbumLikeException();
        }
        User user=usersRepository.findByUserId(userId).get();
        AlbumChat albumChat=albumChatRepository.findById(albumChatId).get();
        AlbumChatAlbumLike like = likeRepository.save(new AlbumChatAlbumLike(user,albumChat));
        return new AlbumChatAlbumLikeReq(likeRepository.save(like).getAlbumChatAlbumLikeId());
    }

    public void albumChatAlbumLikeDelete(Long likeId) {
        if(likeRepository.findById(likeId).isEmpty()) {
            throw new NotFoundLikeException();
        }
        likeRepository.deleteById(likeId);
    }
}