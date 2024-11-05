package org.cosmic.backend.domain.mail.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.cosmic.backend.domain.user.domains.Email;

@Builder
public record EmailAddress(@NotNull String email) {

  public static EmailAddress from(Email email) {
    return new EmailAddress(email.getEmail());
  }
}
