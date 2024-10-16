package org.cosmic.backend.domain.musicProfile.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.UserAlbumChatDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostDetail;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDetail {//post와 작성했던 앨범챗들을 가져옴.
  List<PostAndCommentsDetail> albumPostList;
  List<AlbumChatDetail> albumChatList;

  public static ActivityDetail from(User user) {
    return ActivityDetail.builder()
        .albumPostList(Post.toPostAndCommentDetail(user.getPosts()))
        .albumChatList(AlbumChatDetail.from(user.getAlbumChatComments()))
        .build();
  }
}
