package org.cosmic.backend.domain.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

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
    return UserLoginDetail.builder()
        .email(email)
        .password(password)
        .build();
  }

  public static UserLoginDetail from(User user, String accessToken, String refreshToken) {
    return UserLoginDetail.builder()
        .email(user.getEmail().getEmail())
        .username(user.getUsername())
        .token(accessToken)
        .refreshToken(refreshToken)
        .id(user.getUserId())
        .build();
  }
}
