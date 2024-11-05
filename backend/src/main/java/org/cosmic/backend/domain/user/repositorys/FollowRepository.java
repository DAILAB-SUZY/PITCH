package org.cosmic.backend.domain.user.repositorys;

import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.user.domains.Follow;
import org.cosmic.backend.domain.user.domains.FollowPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface FollowRepository extends JpaRepository<Follow, FollowPK> {

  List<Follow> findAllByUser_UserId(Long userId);

  List<Follow> findAllByOther_UserId(Long userId);

  Optional<Follow> findByUser_UserIdAndOther_UserId(Long userId, Long otherUserId);
}
