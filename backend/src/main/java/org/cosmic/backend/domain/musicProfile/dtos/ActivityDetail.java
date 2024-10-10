package org.cosmic.backend.domain.musicProfile.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostDetail;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDetail {//post와 작성했던 앨범챗들을 가져옴.
    PostDetail postDetail;
    UserAlbumChatDetail albumChatDetail;
}
