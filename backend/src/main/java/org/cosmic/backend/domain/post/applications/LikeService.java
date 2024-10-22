package org.cosmic.backend.domain.post.applications;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.domain.post.entities.PostLike;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.post.repositories.PostLikeRepository;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 좋아요 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. 좋아요 조회, 생성, 삭제 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class LikeService {

  private final PostLikeRepository postLikeRepository;
  private final UsersRepository usersRepository;
  private final PostRepository postRepository;

  @Transactional
  public PostAndCommentsDetail likeOrUnlikePost(Long postId, Long userId) {
    if (postLikeRepository.existsByPost_PostIdAndUser_UserId(postId, userId)) {
      postLikeRepository.deleteByPost_PostIdAndUser_UserId(postId, userId);
    } else {
      postLikeRepository.save(PostLike.builder()
          .post(postRepository.findById(postId).orElseThrow(NotFoundPostException::new))
          .user(usersRepository.findById(userId).orElseThrow(NotFoundUserException::new))
          .build());
    }
    postLikeRepository.flush();
    return PostAndCommentsDetail.from(
        postRepository.findById(postId).orElseThrow(NotFoundPostException::new));
  }
}