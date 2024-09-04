package org.cosmic.backend.domain.bestAlbum.repositorys;

import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbumPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBestAlbumRepository extends JpaRepository<UserBestAlbum, UserBestAlbumPK>
{
    Optional<List<UserBestAlbum>> findByUser_UserId(Long userId);//key로 찾기
    void deleteByUser_UserId(Long userId);
    Optional<UserBestAlbum> findByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);
}
