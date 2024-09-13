package org.cosmic.backend.domain.post.repositories;
import io.lettuce.core.dynamic.annotation.Param;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDetail;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {
    List<PostComment> findByPost_PostId(Long postid);//key로 찾기
    PostComment findByCommentId(Long commentid);
    List<PostComment> findAllByParentComment_CommentId(Long commentId);

    @Query(value = "SELECT new org.cosmic.backend.domain.post.dtos.Comment.CommentDetail(" +
            "p.commentId " +
            ",p.content " +
            ",p.createTime " +
            ",COUNT(1)" +
            ",new org.cosmic.backend.domain.user.dtos.UserDetail(u.userId, u.username, u.profilePicture)" +
            ") " +
            "FROM PostComment p " +
            "LEFT JOIN p.postCommentLikes l " +
            "JOIN p.user u " +
            "WHERE p.post.postId = :postId " +
            "GROUP BY p.commentId, p.content, p.createTime, u.userId, u.username, u.profilePicture")
    List<CommentDetail> findAllWithLikesByPostId(@Param("postId") Long postId);
}
