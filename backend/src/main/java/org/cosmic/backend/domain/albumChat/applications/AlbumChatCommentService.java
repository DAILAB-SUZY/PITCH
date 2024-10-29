package org.cosmic.backend.domain.albumChat.applications;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentRequest;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.exceptions.NotMatchAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * <p>AlbumChatCommentService 클래스는 앨범 챗의 댓글 관리 기능을 제공합니다.</p>
 *
 * <p>댓글 조회, 생성, 수정, 삭제와 관련된 비즈니스 로직을 처리합니다.</p>
 */
@Service
@RequiredArgsConstructor
public class AlbumChatCommentService {

  private final AlbumRepository albumRepository;
  private final AlbumChatCommentRepository commentRepository;
  private final UsersRepository userRepository;
  private final AlbumChatCommentRepository albumChatCommentRepository;

  private List<AlbumChatComment> getCommentsSortedBy(String spotifyAlbumId, String sorted,
      Pageable pageable) {
    if ("recent".equals(sorted)) {
      return commentRepository.findByAlbum_SpotifyAlbumIdOrderByTime(spotifyAlbumId, pageable)
          .getContent();
    }
    return commentRepository.findByAlbum_SpotifyAlbumIdOrderByLike(spotifyAlbumId, pageable)
        .getContent();
  }

  private Pageable getPageable(Integer page, Integer limit) {
    return PageRequest.of(page, limit);
  }

  /**
   * <p>특정 앨범의 앨범챗 댓글을 조회합니다.</p>
   *
   * @param spotifyAlbumId 앨범 ID
   * @param sorted         정렬 기준 (예: 좋아요 순, 최신 순)
   * @return 댓글 목록을 포함한 리스트
   * @throws NotFoundAlbumChatException 앨범을 찾을 수 없을 때 발생합니다.
   */
  private List<AlbumChatCommentDetail> getAlbumChatComment(String spotifyAlbumId, String sorted) {
    return getAlbumChatComment(spotifyAlbumId, sorted, 0, 5);
  }

  public List<AlbumChatCommentDetail> getAlbumChatComment(String spotifyAlbumId, String sorted,
      Integer page,
      Integer limit) {
    return AlbumChatCommentDetail.from(
        getCommentsSortedBy(spotifyAlbumId, sorted, getPageable(page, limit)));
  }

  public AlbumChatCommentDetail getAlbumChat(Long albumChatId) {
    return AlbumChatCommentDetail.from(albumChatCommentRepository.findById(albumChatId)
        .orElseThrow(NotFoundAlbumChatCommentException::new));
  }

  private AlbumChatComment getParentComment(Long commentId) {
    return commentRepository.findById(commentId)
        .orElseThrow(NotFoundAlbumChatCommentException::new);
  }

  /**
   * <p>새로운 앨범챗 댓글을 생성합니다.</p>
   *
   * @param spotifyAlbumId 앨범 ID
   * @param comment        댓글 생성 요청 데이터
   * @param userId         댓글을 작성한 사용자 ID
   * @return 생성된 댓글 목록을 포함한 리스트
   * @throws NotFoundAlbumChatException 앨범을 찾을 수 없을 때 발생합니다.
   * @throws NotFoundUserException      사용자를 찾을 수 없을 때 발생합니다.
   */
  public List<AlbumChatCommentDetail> albumChatCommentCreate(String spotifyAlbumId,
      AlbumChatCommentRequest comment, Long userId) {
    AlbumChatComment albumChatComment = AlbumChatComment.from(
        albumRepository.findBySpotifyAlbumId(spotifyAlbumId)
            .orElseThrow(NotFoundAlbumChatException::new),
        userRepository.findById(userId).orElseThrow(NotFoundUserException::new),
        comment.getContent());
    if (comment.getParentAlbumChatCommentId() != null) {
      albumChatComment.setParentAlbumChatComment(
          getParentComment(comment.getParentAlbumChatCommentId()));
    }
    commentRepository.save(albumChatComment);
    return getAlbumChatComment(spotifyAlbumId, comment.getSorted());
  }

  /**
   * <p>앨범챗 댓글을 수정합니다.</p>
   *
   * @param spotifyAlbumId     앨범 ID
   * @param albumChatCommentId 수정할 댓글 ID
   * @param comment            수정할 댓글 데이터
   * @param userId             수정하는 사용자 ID
   * @return 수정된 댓글 목록을 포함한 리스트
   * @throws NotFoundAlbumChatCommentException 댓글을 찾을 수 없을 때 발생합니다.
   * @throws NotFoundUserException             사용자를 찾을 수 없을 때 발생합니다.
   * @throws NotMatchAlbumChatException        댓글과 앨범 ID가 일치하지 않을 때 발생합니다.
   * @throws NotMatchUserException             댓글을 수정하려는 사용자가 원 작성자가 아닐 때 발생합니다.
   */
  public List<AlbumChatCommentDetail> albumChatCommentUpdate(String spotifyAlbumId,
      Long albumChatCommentId,
      AlbumChatCommentRequest comment, Long userId) {
    AlbumChatComment updatedComment = commentRepository.findById(albumChatCommentId)
        .orElseThrow(NotFoundAlbumChatCommentException::new);
    if (!updatedComment.getAlbum().getSpotifyAlbumId().equals(spotifyAlbumId)) {
      throw new NotMatchAlbumChatException();
    }
    if (!updatedComment.getUser().getUserId().equals(userId)) {
      throw new NotMatchUserException();
    }
    updatedComment.setContent(comment.getContent());
    commentRepository.save(updatedComment);
    return getAlbumChatComment(spotifyAlbumId, comment.getSorted());
  }

  /**
   * <p>앨범챗 댓글을 삭제합니다.</p>
   *
   * @param spotifyAlbumId     앨범 ID
   * @param albumChatCommentId 삭제할 댓글 ID
   * @param sorted             정렬 기준
   * @return 삭제된 후의 댓글 목록을 포함한 리스트
   * @throws NotFoundAlbumChatCommentException 댓글을 찾을 수 없을 때 발생합니다.
   */
  public List<AlbumChatCommentDetail> albumChatCommentDelete(String spotifyAlbumId,
      Long albumChatCommentId,
      String sorted) {
    AlbumChatComment parentComment = commentRepository.findById(albumChatCommentId)
        .orElseThrow(NotFoundAlbumChatCommentException::new);
    commentRepository.delete(parentComment);
    return getAlbumChatComment(spotifyAlbumId, sorted);
  }

  public AlbumChatCommentDetail albumChatCommentGet(String spotifyAlbumId, Long albumChatCommentId,String sorted) {
    AlbumChatComment parentComment = commentRepository.findById(albumChatCommentId)
          .orElseThrow(NotFoundAlbumChatCommentException::new);
    return AlbumChatCommentDetail.from(parentComment);
  }


}
