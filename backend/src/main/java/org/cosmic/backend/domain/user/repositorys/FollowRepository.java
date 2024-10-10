package org.cosmic.backend.domain.user.repositorys;

import org.cosmic.backend.domain.user.domains.Follow;
import org.cosmic.backend.domain.user.domains.FollowPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface FollowRepository extends JpaRepository<Follow, FollowPK> {

}
