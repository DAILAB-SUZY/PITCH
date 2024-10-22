package org.cosmic.backend.domain.post.applications;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.exceptions.NotMatchAlbumException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시물(Post) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. 게시물 조회, 생성, 수정, 삭제 및 앨범/아티스트 검색 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class PostService {

  private final PostRepository postRepository;
  private final UsersRepository userRepository;
  private final AlbumRepository albumRepository;

  private List<PostAndCommentsDetail> getUsersPosts(Long userId, Pageable pageable) {
    return postRepository.findByUser_UserId(userId, pageable).map(PostAndCommentsDetail::from)
        .getContent();
  }

  private Pageable getPostPageable(Integer page, Integer limit) {
    return PageRequest.of(page, limit);
  }

  public List<PostAndCommentsDetail> getPosts(Long userId, Integer page, Integer limit) {
    Pageable pageable = getPostPageable(page, limit);
    if (userId != null) {
      return getUsersPosts(userId, pageable);
    }
    return postRepository.findAllWithCustomSorting(pageable)
        .getContent().stream().map(PostAndCommentsDetail::from).toList();
  }

  /**
   * 특정 게시물 ID로 게시물을 조회합니다.
   *
   * @param postId 조회할 게시물의 ID
   * @return 조회된 게시물의 요청 객체
   * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
   */
  public PostAndCommentsDetail getPostById(Long postId) {
    return PostAndCommentsDetail.from(
        postRepository.findById(postId).orElseThrow(NotFoundPostException::new));
  }

  /**
   * 새로운 게시물을 생성합니다.
   *
   * @return 생성된 게시물의 DTO 객체
   * @throws NotFoundUserException  사용자를 찾을 수 없을 때 발생합니다.
   * @throws NotMatchAlbumException 게시물에 해당하는 앨범을 찾을 수 없을 때 발생합니다.
   */
  @Transactional
  public PostAndCommentsDetail createPost(String content, String spotifyAlbumId, Long userId) {
    return PostAndCommentsDetail.from(postRepository.save(Post.builder()
        .content(content)
        .user(userRepository.findById(userId).orElseThrow(NotFoundUserException::new))
        .album(albumRepository.findBySpotifyAlbumId(spotifyAlbumId)
            .orElseThrow(NotFoundAlbumException::new))
        .build()));
  }

  private void validPostAuthor(Post post, Long userId) {
    if (!post.isAuthorId(userId)) {
      throw new NotMatchUserException();
    }
  }

  /**
   * 기존 게시물을 수정합니다.
   *
   * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
   */
  @Transactional
  public PostAndCommentsDetail updatePost(String content, Long postId, Long userId) {
    Post updatedPost = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
    validPostAuthor(updatedPost, userId);
    updatedPost.setContent(content);
    updatedPost.setUpdateTime(Instant.now());
    return getPostById(postRepository.save(updatedPost).getPostId());
  }

  /**
   * 특정 게시물을 삭제합니다.
   *
   * @param postId 삭제할 게시물의 ID
   * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
   */
  @Transactional
  public void deletePost(Long postId, Long userId) {
    Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
    validPostAuthor(post, userId);
    postRepository.delete(post);
  }
}
