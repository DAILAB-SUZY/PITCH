package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumChatService {
    private final AlbumChatRepository albumChatRepository;
    private final AlbumChatCommentRepository albumChatCommentRepository;

    public AlbumChatService(AlbumChatRepository albumChatRepository, AlbumChatCommentRepository albumChatCommentRepository) {
        this.albumChatRepository = albumChatRepository;
        this.albumChatCommentRepository = albumChatCommentRepository;
    }

    @Transactional
    public AlbumChatResponse getAlbumChatById(AlbumDto album) {
        if(albumChatRepository.findByAlbum_AlbumId(album.getAlbumId()).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        return new AlbumChatResponse(albumChatRepository.findByAlbum_AlbumId(album.getAlbumId()).get());
    }

    @Transactional
    public List<AlbumChatCommentResponse> getAlbumChatCommentByManyLikeId(AlbumChatDto albumChat) {
        if(albumChatRepository.findById(albumChat.getAlbumChatId()).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }

        return albumChatCommentRepository.findByAlbumChatIdOrderByCountAlbumChatCommentLikes(albumChat.getAlbumChatId())
                .orElse(Collections.emptyList())
                .stream()
                .map(AlbumChatCommentResponse::new)
                .collect(Collectors.toList());
    }
}
