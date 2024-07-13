package org.cosmic.backend.domain.bestAlbum.repository;
import org.cosmic.backend.domain.bestAlbum.domain.Album_User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Album_UserRepository extends JpaRepository<Album_User,Long>
{
    Optional<List<Album_User>> findByUser_UserId(Long userId);//key로 찾기
    void deleteByUser_UserId(Long albumId);
    Optional<Album_User> findByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);

}
