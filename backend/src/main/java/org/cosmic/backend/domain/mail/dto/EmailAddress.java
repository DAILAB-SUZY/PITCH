package org.cosmic.backend.domain.mail.dto;

import lombok.Builder;

@Builder
public record EmailAddress(String email) {
}
