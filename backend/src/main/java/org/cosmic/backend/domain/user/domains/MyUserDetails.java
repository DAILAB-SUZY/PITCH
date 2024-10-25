package org.cosmic.backend.domain.user.domains;

import org.springframework.security.core.userdetails.UserDetails;

public interface MyUserDetails extends UserDetails {

  Email getEmail();
}
