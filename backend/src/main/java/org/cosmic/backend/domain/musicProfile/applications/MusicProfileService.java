package org.cosmic.backend.domain.musicProfile.applications;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.bestAlbum.applications.BestAlbumService;
import org.cosmic.backend.domain.favoriteArtist.applications.FavoriteArtistService;
import org.cosmic.backend.domain.musicDna.applications.MusicDnaService;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.user.dtos.MusicProfileDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class MusicProfileService {

    @Autowired
    MusicDnaService musicDnaService;
    @Autowired
    PlaylistService playlistService;
    @Autowired
    BestAlbumService bestAlbumService;
    @Autowired
    FavoriteArtistService favoriteArtistService;
    /**
     * <p>특정 사용자의 뮤직 프로필을 조회합니다.</p>
     *
     * @param userId 조회할 사용자의 ID
     * @return 사용자의 뮤직 프로필 데이터를 포함한 객체
     */
    public MusicProfileDetail openMusicProfile(Long userId) {
        MusicProfileDetail musicProfileDetail = new MusicProfileDetail();
        musicProfileDetail.setUserDna(musicDnaService.getUserDna(userId));
        musicProfileDetail.setPlaylist(playlistService.open(userId));
        musicProfileDetail.setBestAlbum(bestAlbumService.open(userId));
        musicProfileDetail.setFavoriteArtist(favoriteArtistService.favoriteArtistGiveData(userId));
        return musicProfileDetail;
    }
}
