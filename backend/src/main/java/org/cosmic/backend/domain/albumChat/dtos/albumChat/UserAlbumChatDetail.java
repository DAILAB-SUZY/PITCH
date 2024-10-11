package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.dtos.AlbumDetail;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAlbumChatDetail {//특정 유저가 작성한 앨범챗들.
    //어떤 앨범인지도
    private AlbumDetail albumDetail;
    private Long albumChatCommentId;
    private String content;
    private Instant createAt;
    private Instant updateAt;
    private UserDetail author;
}
