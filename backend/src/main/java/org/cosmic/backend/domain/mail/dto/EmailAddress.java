package org.cosmic.backend.domain.mail.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record EmailAddress(@NotNull String email) {
}
