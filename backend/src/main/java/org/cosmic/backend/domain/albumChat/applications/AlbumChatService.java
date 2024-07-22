package org.cosmic.backend.domain.albumChat.applications;
import org.cosmic.backend.domain.albumChat.domains.AlbumChat;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentLikeRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;

import org.cosmic.backend.domain.post.dto.Post.AlbumDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlbumChatService {
    @Autowired
    private AlbumChatRepository albumChatRepository;
    @Autowired
    private AlbumChatCommentRepository albumChatCommentRepository;
    @Autowired
    private AlbumChatCommentLikeRepository albumChatCommentLikeRepository;
    @Autowired
    private AlbumChatCommentService albumChatCommentService;

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

    @Transactional
    public List<AlbumChatCommentResponse> getAlbumChatCommentByManyLikeId(AlbumChatDto albumchat) {//Post가 아니라 post에서 나와야할 내용들이 리턴되야함

        if(!albumChatCommentRepository.findByAlbumChat_AlbumChatId(albumchat.getAlbumChatId()).isPresent())
        {
            throw new NotFoundAlbumChatException();
        }
        else{
            List<AlbumChatComment> albumChatCommentList= albumChatCommentRepository.findByAlbumChat_AlbumChatId(albumchat.getAlbumChatId()).get();
            Map<AlbumChatComment,Long>albumMap=new HashMap<>();
            for(AlbumChatComment albumChatComment:albumChatCommentList){
                Long likeCount =albumChatCommentLikeRepository.countByAlbumChatComment_AlbumChatCommentId(albumChatComment.getAlbumChatCommentId());
                albumMap.put(albumChatComment,likeCount);
            }

            List<Map.Entry<AlbumChatComment, Long>> sortedComments = albumMap.entrySet()
                    .stream()
                    .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                    .collect(Collectors.toList());

            return albumChatCommentService.sortedAlbumChatComment(sortedComments);
        }
    }
}
