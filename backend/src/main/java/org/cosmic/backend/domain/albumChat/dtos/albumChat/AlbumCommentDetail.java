package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AlbumCommentDetail {//특정 유저가 작성한 앨범챗들.
  //어떤 앨범인지도
  AlbumDetail albumDetail;
  AlbumChatCommentDetail albumChatCommentDetail;

  public static List<AlbumCommentDetail> fromNullParentAlbumChat(List<AlbumChatComment> albumChatComments) {
    return albumChatComments.stream()
            .filter(comment -> comment.getParentAlbumChatComment() == null)  // parent가 null인 경우만 필터링
            .map(AlbumCommentDetail::from)
            .toList();
  }
  public static AlbumCommentDetail from(AlbumChatComment albumChatComment)
  {
    return AlbumCommentDetail.builder()
      .albumDetail(AlbumDetail.from(albumChatComment.getAlbum()))
      .albumChatCommentDetail(new AlbumChatCommentDetail(albumChatComment))
      .build();
  }

  public static List<AlbumCommentDetail> from(List<AlbumChatComment> albumChatComments) {
    return albumChatComments.stream().map(AlbumCommentDetail::from).toList();
  }



}
