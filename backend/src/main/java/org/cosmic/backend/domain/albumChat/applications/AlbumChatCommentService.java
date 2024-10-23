package org.cosmic.backend.domain.albumChat.applications;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentRequest;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.exceptions.NotMatchAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentLikeRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
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
  private final AlbumChatCommentLikeRepository commentLikeRepository;
  private final UsersRepository userRepository;

  /**
   * <p>특정 앨범의 앨범챗 댓글을 조회합니다.</p>
   *
   * @param albumId 앨범 ID
   * @param sorted  정렬 기준 (예: 좋아요 순, 최신 순)
   * @param count   조회할 댓글 수
   * @return 댓글 목록을 포함한 리스트
   * @throws NotFoundAlbumChatException 앨범을 찾을 수 없을 때 발생합니다.
   */
  public AlbumChatDetail getAlbumChatComment(Long albumId, String sorted, int count) {
    if (albumRepository.findById(albumId).isEmpty()) {
      throw new NotFoundAlbumChatException();
    }
//    List<AlbumChatCommentDetail> albumChatCommentDetails = new ArrayList<>();
//    if (sorted.equals("manylike")) {
//      albumChatCommentDetails = getAlbumChatCommentByManyLikeId(albumId, count);
//    } else if (sorted.equals("recent")) {
//      albumChatCommentDetails = getAlbumChatCommentRecentId(albumId, count);
//    }
    return AlbumChatDetail.from(albumRepository.findById(albumId).get());
  }

//  /**
//   * <p>좋아요 순으로 정렬된 앨범챗 댓글을 조회합니다.</p>
//   *
//   * @param albumId 앨범 ID
//   * @param count   조회할 댓글 수
//   * @return 댓글 목록을 포함한 리스트
//   */
//  public List<AlbumChatCommentDetail> getAlbumChatCommentByManyLikeId(Long albumId, int count) {
//    List<AlbumChatCommentDetail> albumChatCommentDetails = commentRepository
//        .findByAlbumIdOrderByCountAlbumChatCommentLikes(albumId, 10 * count)
//        .orElse(Collections.emptyList())
//        .stream()
//        .map(AlbumChatCommentDetail::new)
//        .collect(Collectors.toList());
//    return getReplyAndCommentLike(albumChatCommentDetails, albumId);
//  }
//
//  /**
//   * <p>최신 순으로 정렬된 앨범챗 댓글을 조회합니다.</p>
//   *
//   * @param albumId 앨범 ID
//   * @param count   조회할 댓글 수
//   * @return 댓글 목록을 포함한 리스트
//   */
//  public List<AlbumChatCommentDetail> getAlbumChatCommentRecentId(Long albumId, int count) {
//    List<AlbumChatCommentDetail> albumChatCommentDetails = commentRepository
//        .findByAlbumIdOrderByRecentAlbumChatCommentLikes(albumId, 10 * count)
//        .orElse(Collections.emptyList())
//        .stream()
//        .map(AlbumChatCommentDetail::new)
//        .collect(Collectors.toList());
//    return getReplyAndCommentLike(albumChatCommentDetails, albumId);
//  }

  private AlbumChatComment getParentId(Long commentId) {
    if(commentId==null) {
      return null;
    }
    else{
      return commentRepository.findById(commentId).get();
    }
  }

  /**
   * <p>새로운 앨범챗 댓글을 생성합니다.</p>
   *
   * @param albumId 앨범 ID
   * @param comment 댓글 생성 요청 데이터
   * @param userId  댓글을 작성한 사용자 ID
   * @return 생성된 댓글 목록을 포함한 리스트
   * @throws NotFoundAlbumChatException 앨범을 찾을 수 없을 때 발생합니다.
   * @throws NotFoundUserException      사용자를 찾을 수 없을 때 발생합니다.
   */
  public AlbumChatDetail albumChatCommentCreate(Long albumId,
      AlbumChatCommentRequest comment, Long userId) {
    AlbumChatComment albumChatComment = AlbumChatComment.from(
      albumRepository.findById(albumId).orElseThrow(NotFoundAlbumChatException::new),
      userRepository.findById(userId).orElseThrow(NotFoundUserException::new),
      comment.getContent());
      if(getParentId(comment.getParentAlbumChatCommentId())!=null)
      {
        albumChatComment.setParentAlbumChatComment(getParentId(comment.getParentAlbumChatCommentId()));
      }
      else{
        albumChatComment.setParentAlbumChatComment(null);
      }
    commentRepository.save(albumChatComment);
    return getAlbumChatComment(albumId, comment.getSorted(), comment.getCount());
  }

  /**
   * <p>앨범챗 댓글을 수정합니다.</p>
   *
   * @param albumId            앨범 ID
   * @param albumChatCommentId 수정할 댓글 ID
   * @param comment            수정할 댓글 데이터
   * @param userId             수정하는 사용자 ID
   * @return 수정된 댓글 목록을 포함한 리스트
   * @throws NotFoundAlbumChatCommentException 댓글을 찾을 수 없을 때 발생합니다.
   * @throws NotFoundUserException             사용자를 찾을 수 없을 때 발생합니다.
   * @throws NotMatchAlbumChatException        댓글과 앨범 ID가 일치하지 않을 때 발생합니다.
   * @throws NotMatchUserException             댓글을 수정하려는 사용자가 원 작성자가 아닐 때 발생합니다.
   */
  public AlbumChatDetail albumChatCommentUpdate(Long albumId, Long albumChatCommentId,
      AlbumChatCommentRequest comment, Long userId) {
    AlbumChatComment updatedComment = commentRepository.findById(albumChatCommentId)
        .orElseThrow(NotFoundAlbumChatCommentException::new);
    if (!updatedComment.getAlbum().getAlbumId().equals(albumId)) {
      throw new NotMatchAlbumChatException();
    }
    if (!updatedComment.getUser().getUserId().equals(userId)) {
      throw new NotMatchUserException();
    }
    updatedComment.setContent(comment.getContent());
    commentRepository.save(updatedComment);
    return getAlbumChatComment(albumId, comment.getSorted(), comment.getCount());
  }

  /**
   * <p>앨범챗 댓글을 삭제합니다.</p>
   *
   * @param albumId            앨범 ID
   * @param albumChatCommentId 삭제할 댓글 ID
   * @param sorted             정렬 기준
   * @param count              조회할 댓글 수
   * @return 삭제된 후의 댓글 목록을 포함한 리스트
   * @throws NotFoundAlbumChatCommentException 댓글을 찾을 수 없을 때 발생합니다.
   */
  public AlbumChatDetail albumChatCommentDelete(Long albumId, Long albumChatCommentId,
      String sorted, int count) {
    AlbumChatComment parentComment = commentRepository.findById(albumChatCommentId)
        .orElseThrow(NotFoundAlbumChatCommentException::new);
    commentRepository.deleteAllByParentAlbumChatComment_AlbumChatCommentId(
        parentComment.getAlbumChatCommentId());
    commentRepository.delete(parentComment);
    return getAlbumChatComment(albumId, sorted, count);
  }
}
