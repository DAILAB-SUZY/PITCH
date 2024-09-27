package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.*;
import org.cosmic.backend.domain.post.dtos.Comment.ChildCommentDetail;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDetail;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dtos.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_comment") // 테이블 이름 수정
@Builder
@EqualsAndHashCode(of = {"commentId", "parentComment"})
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id") // 컬럼 이름 명시
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    private PostComment parentComment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    @Column(name = "create_time")
    @Builder.Default
    private Instant createTime = Instant.now();

    @Column(name = "update_time")
    private Instant updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostCommentLike> postCommentLikes = new ArrayList<>();

    public static CommentDetail toCommentDetail(PostComment comment) {
        return CommentDetail.builder()
                .id(comment.commentId)
                .content(comment.content)
                .createdAt(comment.createTime)
                .updatedAt(comment.updateTime)
                .author(User.toUserDetail(comment.getUser()))
                .likes(comment.postCommentLikes.stream().map(like -> User.toUserDetail(like.getUser())).toList())
                .build();
    }

    public static List<CommentDetail> toCommentDetails(List<PostComment> comments) {
        Map<PostComment, List<PostComment>> groupedByParent = new HashMap<>();
        comments.stream()
                .sorted(Comparator.comparing(PostComment::getCreateTime))
                .forEach(comment -> {
                    if(comment.getParentComment() == null || !groupedByParent.containsKey(comment.getParentComment())){
                        groupedByParent.put(comment, new ArrayList<>());
                    }
                    else {
                        groupedByParent.get(comment.getParentComment()).add(comment);
                    }
                });

        return groupedByParent.entrySet().stream()
                .map(entry -> {
                    PostComment parent = entry.getKey();
                    List<ChildCommentDetail> childComments = entry.getValue().stream()
                            .map(PostComment::toChildCommentDetail)
                            .toList();
                    CommentDetail commentDetail = PostComment.toCommentDetail(parent);
                    commentDetail.setChildComments(childComments);
                    return commentDetail;
                })
                .toList();
    }

    public static CommentDto toCommentDto(PostComment postComment) {
        return CommentDto.builder()
                .commentId(postComment.getCommentId())
                .build();
    }

    public static UpdateReplyReq toUpdateReplyReq(PostComment postComment) {
        return UpdateReplyReq.builder()
                .content(postComment.getContent())
                .content(postComment.getContent())
                .build();
    }

    public static ReplyDto toReplyDto(PostComment postComment) {
        return ReplyDto.builder()
                .replyId(postComment.commentId)
                .build();
    }

    public static ChildCommentDetail toChildCommentDetail(PostComment postComment) {
        return ChildCommentDetail.builder()
                .id(postComment.commentId)
                .content(postComment.content)
                .author(User.toUserDetail(postComment.getUser()))
                .build();
    }
}
