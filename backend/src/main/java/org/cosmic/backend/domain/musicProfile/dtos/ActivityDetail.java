package org.cosmic.backend.domain.musicProfile.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.UserAlbumChatDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostDetail;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDetail {//post와 작성했던 앨범챗들을 가져옴.
  List<PostDetail> postDetail;
  List<UserAlbumChatDetail> albumChatDetail;

  public static ActivityDetail from(User user) {
    return ActivityDetail.builder()
        .postDetail(PostDetail.from(user.getPosts()))
        .albumChatDetail(UserAlbumChatDetail.from(user.getAlbumChatComments()))
        .build();
  }
}
