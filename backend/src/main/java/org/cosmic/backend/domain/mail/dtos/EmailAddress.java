package org.cosmic.backend.domain.mail.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record EmailAddress(@NotNull String email) {
}
