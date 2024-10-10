package org.cosmic.backend.domain.musicProfile.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicProfileDetail {
    private UserDetail userDetail;
    private FavoriteArtistDetail favoriteArtist;
    private List<BestAlbumDetail> bestAlbum;
    private List<UserDnaResponse> userDna;
    private List<PlaylistDetail> playlist;
}
