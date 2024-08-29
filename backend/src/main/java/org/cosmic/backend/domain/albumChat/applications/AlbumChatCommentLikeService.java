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

@Service
public class AlbumChatCommentLikeService {
    private final AlbumChatCommentLikeRepository likeRepository;
    private final UsersRepository usersRepository;
    private final AlbumChatCommentRepository albumChatCommentRepository;

    public AlbumChatCommentLikeService(AlbumChatCommentLikeRepository likeRepository, UsersRepository usersRepository, AlbumChatCommentRepository albumChatCommentRepository) {
        this.likeRepository = likeRepository;
        this.usersRepository = usersRepository;
        this.albumChatCommentRepository = albumChatCommentRepository;
    }

    public List<AlbumChatCommentLikeResponse> getAlbumChatCommentLikeByAlbumChatCommentId(Long albumChatCommentId) {
        if(albumChatCommentRepository.findById(albumChatCommentId).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        return likeRepository.findByAlbumChatComment_AlbumChatCommentId(albumChatCommentId)
            .stream()
            .map(AlbumChatCommentLikeResponse::new)
            .collect(Collectors.toList());
    }

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

    public void albumChatCommentLikeDelete(Long likeId) {
        if(likeRepository.findById(likeId).isEmpty()) {
            throw new NotFoundCommentLikeException();
        }
        likeRepository.deleteById(likeId);
    }
}
