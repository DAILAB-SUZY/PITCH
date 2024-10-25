package org.cosmic.backend.domain.albumChat.applications;

import java.util.List;
import java.util.stream.Collectors;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentLikeRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.globals.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>AlbumChatCommentLikeService 클래스는 앨범 챗 댓글에 대한 좋아요 기능을 관리하는 서비스 클래스입니다.</p>
 *
 * <p>이 서비스는 댓글에 대한 좋아요의 생성, 조회, 삭제 기능을 제공합니다.</p>
 */
@Service
public class AlbumChatCommentLikeService {

  private final AlbumChatCommentLikeRepository albumChatCommentLikeRepository;
  private final UsersRepository usersRepository;
  private final AlbumChatCommentRepository albumChatCommentRepository;

  /**
   * <p>AlbumChatCommentLikeService 생성자입니다.</p>
   *
   * @param albumChatCommentLikeRepository 앨범 챗 댓글 좋아요 리포지토리
   * @param usersRepository                사용자 리포지토리
   * @param albumChatCommentRepository     앨범 챗 댓글 리포지토리
   */
  public AlbumChatCommentLikeService(AlbumChatCommentLikeRepository albumChatCommentLikeRepository,
      UsersRepository usersRepository,
      AlbumChatCommentRepository albumChatCommentRepository) {
    this.albumChatCommentLikeRepository = albumChatCommentLikeRepository;
    this.usersRepository = usersRepository;
    this.albumChatCommentRepository = albumChatCommentRepository;
  }

  /**
   * <p>특정 앨범챗 댓글에 대한 좋아요 목록을 조회합니다.</p>
   *
   * @param albumChatCommentId 조회할 댓글 ID
   * @return 좋아요 목록을 포함한 리스트
   * @throws NotFoundAlbumChatCommentException 앨범챗 댓글을 찾을 수 없을 때 발생합니다.
   */
  public List<AlbumChatCommentLikeDetail> getAlbumChatCommentLikeByAlbumChatCommentId(
      Long albumChatCommentId) {
    if (albumChatCommentRepository.findById(albumChatCommentId).isEmpty()) {
      throw new NotFoundAlbumChatCommentException();
    }
    return albumChatCommentLikeRepository.findByAlbumChatComment_AlbumChatCommentId(
            albumChatCommentId)
        .stream()
        .map(AlbumChatCommentLikeDetail::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void like(Long albumChatCommentId, Long userId) {
    albumChatCommentLikeRepository.save(
        AlbumChatCommentLike.from(
            albumChatCommentRepository.findById(albumChatCommentId)
                .orElseThrow(NotFoundAlbumChatCommentException::new),
            usersRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new)
        )
    );
  }

  @Transactional
  public void unlike(Long albumChatCommentId, Long userId) {
    AlbumChatCommentLike albumChatcommentLike = albumChatCommentLikeRepository.findByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(
            albumChatCommentId, userId)
        .orElseThrow(() -> new NotFoundException("like dont found"));
    albumChatCommentLikeRepository.delete(albumChatcommentLike);
  }
}
