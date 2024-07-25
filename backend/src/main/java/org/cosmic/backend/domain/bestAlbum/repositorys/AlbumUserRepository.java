package org.cosmic.backend.domain.bestAlbum.repositorys;
import org.cosmic.backend.domain.bestAlbum.domains.AlbumUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumUserRepository extends JpaRepository<AlbumUser,Long>
{
    Optional<List<AlbumUser>> findByUser_UserId(Long userId);//key로 찾기
    void deleteByUser_UserId(Long albumId);
    Optional<AlbumUser> findByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);

}
