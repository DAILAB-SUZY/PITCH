package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;

@Builder
@Data
@AllArgsConstructor
public class AlbumChatDetail {

  AlbumDetail albumDetail;
  private List<AlbumChatCommentDetail> comments;

  public static AlbumChatDetail from(Album album) {
    return AlbumChatDetail.builder()
        .albumDetail(AlbumDetail.from(album))
        .comments(AlbumChatCommentDetail.from(album.getAlbumChatComments()))
        .build();
  }

  public static List<AlbumChatDetail> from(List<Album> albums) {
    return albums.stream().map(AlbumChatDetail::from).toList();
  }

  public static AlbumChatDetail from(Album album, List<AlbumChatComment> commentsSortedBy) {
    return AlbumChatDetail.builder()
        .albumDetail(AlbumDetail.from(album))
        .comments(AlbumChatCommentDetail.from(commentsSortedBy))
        .build();
  }
}
