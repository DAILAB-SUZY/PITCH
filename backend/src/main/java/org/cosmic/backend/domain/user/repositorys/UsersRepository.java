package org.cosmic.backend.domain.user.repositorys;

import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.user.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail_Email(String email);//이메일로 찾기

  Optional<User> findByUserId(Long userId);//key로 찾기

  @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT('%', :name, '%') ORDER BY u.userId")
  Optional<List<User>> findByUsername(@Param("name") String name);

  boolean existsByEmail_Email(String email);
}
