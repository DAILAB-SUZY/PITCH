package org.cosmic.backend.domain.post.applications;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.exceptions.NotMatchAlbumException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostDetail;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.domains.User;
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
    return postRepository.findByUser_UserId(userId, pageable).map(Post::toPostAndCommentDetail)
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
        .getContent().stream().map(Post::toPostAndCommentDetail).toList();
  }

  /**
   * 특정 게시물 ID로 게시물을 조회합니다.
   *
   * @param postId 조회할 게시물의 ID
   * @return 조회된 게시물의 요청 객체
   * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
   */
  public PostAndCommentsDetail getPostById(Long postId) {
    if (postRepository.findById(postId).isEmpty()) {
      throw new NotFoundPostException();
    }
    return Post.toPostAndCommentDetail(postRepository.findById(postId).get());
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
    User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    Album album = albumRepository.findBySpotifyAlbumId(spotifyAlbumId)
        .orElseThrow(NotFoundAlbumException::new);

    Post post = postRepository.save(Post.builder()
        .content(content)
        .user(user)
        .album(album)
        .build());
    return Post.toPostAndCommentDetail(post);
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
    return Post.toPostAndCommentDetail(postRepository.save(updatedPost));
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

  @Transactional
  public List<PostDetail> openPost(Long userId) {
    List<PostDetail> postDetails = new ArrayList<>();
    List<Post> posts = new ArrayList<>();
    posts = postRepository.findByUser_UserId(userId);
    for (int i = 0; i < posts.size(); i++) {
      PostDetail postDetail = new PostDetail(posts.get(i).getPostId(), posts.get(i).getContent(),
          posts.get(i).getCreateTime(), posts.get(i).getUpdateTime(),
          User.toUserDetail(userRepository.findById(userId).get()),
          Album.toAlbumDetail(posts.get(i).getAlbum()));//user album

      postDetails.add(postDetail);
    }
    return postDetails;
  }
}
