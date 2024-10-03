package org.cosmic.backend.domain.user.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.user.domains.User;

import java.util.List;

@Data
@NoArgsConstructor
public class MusicProfileDetail {
    private UserDetail userDetail;
    private FavoriteArtistDetail favoriteArtist;
    private List<BestAlbumDetail> bestAlbum;
    private List<UserDnaResponse> userDna;
    private List<PlaylistDetail> playlist;
}
