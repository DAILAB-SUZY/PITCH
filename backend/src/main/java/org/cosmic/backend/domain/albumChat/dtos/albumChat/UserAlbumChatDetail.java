package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserAlbumChatDetail {//특정 유저가 작성한 앨범챗들.
  //어떤 앨범인지도
  private AlbumDetail albumDetail;
  private Long albumChatCommentId;
  private String content;
  private Instant createAt;
  private Instant updateAt;
  private UserDetail author;

  public static UserAlbumChatDetail from(AlbumChatComment albumChatComment) {
    return UserAlbumChatDetail.builder()
        .albumDetail(AlbumDetail.from(albumChatComment.getAlbum()))
        .albumChatCommentId(albumChatComment.getAlbumChatCommentId())
        .content(albumChatComment.getContent())
        .createAt(albumChatComment.getCreateTime())
        .updateAt(albumChatComment.getUpdateTime())
        .author(UserDetail.from(albumChatComment.getUser()))
        .build();
  }

  public static List<UserAlbumChatDetail> from(List<AlbumChatComment> albumChatComments) {
    return albumChatComments.stream().map(UserAlbumChatDetail::from).toList();
  }
}
