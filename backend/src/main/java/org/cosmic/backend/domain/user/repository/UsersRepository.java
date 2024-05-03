package org.cosmic.backend.domain.user.repository;

import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail_Email(String email);//이메일로 찾기
    Boolean findByPassword(String password);
}
