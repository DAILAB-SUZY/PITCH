package org.cosmic.backend.domain.albumChat.applications;

import lombok.AllArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentLikeRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.dtos.AlbumDto;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AlbumChat페이지와 관련된 기능들을 제공하는 서비스 클래스.
 */
@Service
public class AlbumChatService {
    @Autowired
    AlbumChatCommentService albumChatCommentService;
    private final AlbumRepository albumRepository;
    /**
     * AlbumChatService 생성자.
     *
     */
    public AlbumChatService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    /**
     * 주어진 앨범 ID에 해당하는 AlbumChat을 반환합니다.
     *
     * @return 앨범 및 아티스트 정보를 포함한 AlbumChatResponse 객체
     * @throws NotFoundAlbumChatException 앨범 채팅을 찾을 수 없는 경우 발생
     */
    @Transactional
    public AlbumChatDetail getAlbumChatById(AlbumDto album) {
        if(albumRepository.findById(album.getAlbumId()).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        AlbumChatDetail albumChatDetail=new AlbumChatDetail(albumRepository.findById(album.getAlbumId()).get());
        List<AlbumChatCommentDetail> albumChatCommentDetails=albumChatCommentService.getAlbumChatCommentRecentId(album.getAlbumId(),0);
        albumChatDetail.setComments(albumChatCommentDetails);
        return albumChatDetail;
    }

}
