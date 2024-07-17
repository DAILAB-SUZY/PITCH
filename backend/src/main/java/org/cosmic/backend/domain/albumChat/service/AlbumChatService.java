package org.cosmic.backend.domain.albumChat.service;
import org.cosmic.backend.domain.albumChat.domain.AlbumChat;
import org.cosmic.backend.domain.albumChat.domain.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dto.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dto.albumChat.AlbumChatManyLikeListResponse;
import org.cosmic.backend.domain.albumChat.dto.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.exception.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatCommentLikeRepository;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatCommentRepository;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatRepository;

import org.cosmic.backend.domain.post.dto.Post.AlbumDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<AlbumChatManyLikeListResponse> getAlbumChatCommentByManyLikeId(AlbumChatDto albumchat) {//Post가 아니라 post에서 나와야할 내용들이 리턴되야함
        List<AlbumChatManyLikeListResponse> albumChatManyLikeListResponses=new ArrayList<>();

        if(!albumChatCommentRepository.findByAlbumChat_AlbumChatId(albumchat.getAlbumChatId()).isPresent())
        {
            throw new NotFoundAlbumChatException();
        }
        else{
            AlbumChatManyLikeListResponse albumChatManyLikeListResponse=new AlbumChatManyLikeListResponse();
            List<AlbumChatComment> albumChatCommentList= albumChatCommentRepository.findByAlbumChat_AlbumChatId(albumchat.getAlbumChatId()).get();
            //해당 앨범챗의 댓글들을 모두 가져온 후
            Map<AlbumChatComment,Long>albumMap=new HashMap<>();
            for(AlbumChatComment albumChatComment:albumChatCommentList){
                Long likeCount =albumChatCommentLikeRepository.countByAlbumChatComment_AlbumChatCommentId(albumChatComment.getAlbumChatCommentId());
                albumMap.put(albumChatComment,likeCount);
            }

            List<Map.Entry<AlbumChatComment, Long>> sortedComments = albumMap.entrySet()
                    .stream()
                    .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                    .collect(Collectors.toList());

            for (Map.Entry<AlbumChatComment, Long> entry : sortedComments) {
                AlbumChatComment comment = entry.getKey();
                long likeCount = entry.getValue();

                AlbumChatManyLikeListResponse response = new AlbumChatManyLikeListResponse();
                response.setAlbumChatCommentId(comment.getAlbumChatCommentId());
                response.setLikeCount(likeCount);
                // 필요한 다른 필드들을 설정

                albumChatManyLikeListResponses.add(response);
            }
            return albumChatManyLikeListResponses;
        }
    }
}
