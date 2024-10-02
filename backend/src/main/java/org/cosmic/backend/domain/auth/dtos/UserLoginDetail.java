package org.cosmic.backend.domain.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDetail {
    private String refreshToken;
    private String token;
    private String email;
    private String password;
    private String username;
    private Long id;
    public static UserLoginDetail createUserLoginDetail(String email, String password) {
        return  UserLoginDetail.builder()
                .email(email)
                .password(password)
                .build();
    }
}
