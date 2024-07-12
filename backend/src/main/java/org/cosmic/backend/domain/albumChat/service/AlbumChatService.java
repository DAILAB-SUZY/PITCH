package org.cosmic.backend.domain.albumChat.service;
import org.cosmic.backend.domain.albumChat.domain.AlbumChat;
import org.cosmic.backend.domain.albumChat.dto.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.exception.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatRepository;

import org.cosmic.backend.domain.post.dto.Post.AlbumDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlbumChatService {
    @Autowired
    private AlbumChatRepository albumChatRepository;

    @Transactional
    public AlbumChatResponse getAlbumChatById(AlbumDto album) {//Post가 아니라 post에서 나와야할 내용들이 리턴되야함
        AlbumChatResponse albumChatResponse =new AlbumChatResponse();
        if(!albumChatRepository.findByAlbum_AlbumId(album.getAlbumId()).isPresent())
        {
            throw new NotFoundAlbumChatException();
        }
        else{
            AlbumChat albumChat=albumChatRepository.findByAlbum_AlbumId(album.getAlbumId()).get();
            albumChatResponse.setAlbumChatId(albumChat.getAlbumChatId());
            albumChatResponse.setTitle(albumChat.getTitle());
            albumChatResponse.setCover(albumChat.getCover());
            albumChatResponse.setGenre(albumChat.getGenre());
            albumChatResponse.setArtistName(albumChat.getArtistName());
            return albumChatResponse;
        }
    }
}
