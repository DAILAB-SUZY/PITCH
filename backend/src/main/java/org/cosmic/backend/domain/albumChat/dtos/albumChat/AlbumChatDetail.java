package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Builder
@Data
@AllArgsConstructor
public class AlbumChatDetail {

  AlbumDetail albumDetail;
  private List<AlbumChatCommentDetail> comments;
  private List<UserDetail> albumLike;

  public static AlbumChatDetail from(Album album) {
    return AlbumChatDetail.builder()
        .albumDetail(AlbumDetail.from(album))
        .comments(AlbumChatCommentDetail.from(album.getAlbumChatComments()))
        .albumLike(UserDetail.from(album.getAlbumLike().stream().map(AlbumLike::getUser).toList()))
        .build();
  }
}
