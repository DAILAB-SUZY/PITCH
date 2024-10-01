package org.cosmic.backend.domain.playList.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerPlaylistDetail {
    Long playlistId;
    List<String> albumCover;
    UserDetail author;

    public FollowerPlaylistDetail(Playlist playlist) {
        this.playlistId=playlist.getPlaylistId();
        this.author= User.toUserDetail(playlist.getUser());
    }
}
