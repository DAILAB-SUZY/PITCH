package org.cosmic.backend.domain.user.repository;

import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email,String> {
    Optional<Email> findByEmail(String email);//이메일로 찾기
}
