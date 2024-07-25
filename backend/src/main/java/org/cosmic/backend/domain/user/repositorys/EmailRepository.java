package org.cosmic.backend.domain.user.repositorys;

import org.cosmic.backend.domain.user.domains.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email,String> {
    Optional<Email> findByEmail(String email);//이메일로 찾기
}
