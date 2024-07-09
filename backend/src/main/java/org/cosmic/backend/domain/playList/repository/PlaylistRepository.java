package org.cosmic.backend.domain.playList.repository;
import org.cosmic.backend.domain.playList.domain.Playlist;
import org.cosmic.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    Playlist findByuser(User user);
}