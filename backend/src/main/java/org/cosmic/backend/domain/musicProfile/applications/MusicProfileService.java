package org.cosmic.backend.domain.musicProfile.applications;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.bestAlbum.applications.BestAlbumService;
import org.cosmic.backend.domain.favoriteArtist.applications.FavoriteArtistService;
import org.cosmic.backend.domain.musicDna.applications.MusicDnaService;
import org.cosmic.backend.domain.musicProfile.dtos.ActivityDetail;
import org.cosmic.backend.domain.musicProfile.dtos.ProfileDetail;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.musicProfile.dtos.MusicProfileDetail;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
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
    @Autowired
    UsersRepository usersRepository;
    /**
     * <p>특정 사용자의 뮤직 프로필을 조회합니다.</p>
     *
     * @param userId 조회할 사용자의 ID
     * @return 사용자의 뮤직 프로필 데이터를 포함한 객체
     */
    public MusicProfileDetail openMusicProfile(Long userId) {
        MusicProfileDetail musicProfileDetail = new MusicProfileDetail();
        musicProfileDetail.setUserDetail(User.toUserDetail(usersRepository.findByUserId(userId).get()));
        musicProfileDetail.setUserDna(musicDnaService.getUserDna(userId));
        musicProfileDetail.setPlaylist(playlistService.open(userId));
        musicProfileDetail.setBestAlbum(bestAlbumService.open(userId));
        musicProfileDetail.setFavoriteArtist(favoriteArtistService.favoriteArtistGiveData(userId));
        return musicProfileDetail;
    }

    public ProfileDetail openProfile(Long userId) {
        ProfileDetail profileDetail = new ProfileDetail();
        profileDetail.setBestAlbum(bestAlbumService.open(userId));
        profileDetail.setFavoriteArtist(favoriteArtistService.favoriteArtistGiveData(userId));
        return profileDetail;
    }
    public ActivityDetail openActivity(Long userId) {
        ActivityDetail activityDetail=new ActivityDetail();
        //해당 유저가 작성했던 포스트랑 앨범챗들을 가져옴.
        activityDetail.set
        return activityDetail;
    }

}
