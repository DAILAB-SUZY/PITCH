package org.cosmic.backend.domain.auth.dtos;

public record PasswordChangeForm(String authCode, String password) {

}
