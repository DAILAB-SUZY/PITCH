package org.cosmic.backend.domain.playList.repositorys;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.user.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    Playlist findByuser(User user);
}