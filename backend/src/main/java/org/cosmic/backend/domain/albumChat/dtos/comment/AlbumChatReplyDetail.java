package org.cosmic.backend.domain.albumChat.dtos.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatReplyDetail implements Serializable {
    private Long albumChatCommentId;
    private String content;
    private Instant createAt;
    private Instant updateAt;
    private List<AlbumChatCommentLikeDetail> likes;
    private UserDetail author;

    public AlbumChatReplyDetail(AlbumChatComment albumChatComment) {
        this.albumChatCommentId = albumChatComment.getAlbumChatCommentId();
        this.content = albumChatComment.getContent();
        this.createAt = albumChatComment.getCreateTime();
        this.updateAt = albumChatComment.getUpdateTime();
        this.likes = albumChatComment.getAlbumChatCommentLikes().stream()
                .map(AlbumChatCommentLike::toAlbumChatCommentLikeDetail).toList();
        this.author = User.toUserDetail(albumChatComment.getUser());
    }
}