package org.cosmic.backend.domain.mail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;

@JsonSerialize
public record VerificationForm(@NotNull @JsonProperty String email, @JsonProperty @NotNull String code) {
}
