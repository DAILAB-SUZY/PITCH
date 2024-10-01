package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.playList.dtos.AlbumDto;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>AlbumChat 페이지와 관련된 기능들을 제공하는 서비스 클래스입니다.</p>
 *
 * <p>이 서비스는 특정 앨범의 앨범챗 데이터를 가져오고, 최근 댓글 목록을 조회하는 기능을 제공합니다.</p>
 *
 */
@Service
public class AlbumChatService {

    @Autowired
    private AlbumChatCommentService albumChatCommentService;

    private final AlbumRepository albumRepository;

    /**
     * <p>AlbumChatService 생성자입니다.</p>
     *
     * @param albumRepository 앨범 데이터를 처리하는 리포지토리
     */
    public AlbumChatService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    /**
     * <p>특정 앨범의 앨범챗 데이터를 조회합니다.</p>
     *
     * @param albumId 앨범 Id
     * @return 앨범챗 세부 정보를 포함한 {@link AlbumChatDetail}
     *
     * @throws NotFoundAlbumChatException 앨범챗을 찾을 수 없을 때 발생합니다.
     */
    @Transactional
    public AlbumChatDetail getAlbumChatById(Long albumId) {
        if (albumRepository.findById(albumId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        AlbumChatDetail albumChatDetail = new AlbumChatDetail(albumRepository.findById(albumId).get());
        List<AlbumChatCommentDetail> albumChatCommentDetails = albumChatCommentService.getAlbumChatCommentRecentId(albumId, 0);
        albumChatDetail.setComments(albumChatCommentDetails);
        return albumChatDetail;
    }
}
