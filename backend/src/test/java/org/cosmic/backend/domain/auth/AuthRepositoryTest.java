package org.cosmic.backend.domain.auth;

import org.cosmic.backend.AbstractContainerBaseTest;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.auth.exceptions.CredentialNotMatchException;
import org.cosmic.backend.domain.user.domains.MyUserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

@Import(TokenProvider.class)
public class AuthRepositoryTest extends AbstractContainerBaseTest {

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Test
  @DisplayName("엑세스 토큰 생성")
  public void createAccessTokenTest() {
    MyUserDetails user = creator.createAndSaveUser("testman");
    String accessToken = tokenProvider.create(user);

    Assertions.assertDoesNotThrow(() -> tokenProvider.validateAndGetId(accessToken));
  }

  @Test
  @DisplayName("리프레쉬 토큰 생성")
  public void validateAndGetIdTest() {
    MyUserDetails user = creator.createAndSaveUser("testman");
    String refreshToken = tokenProvider.createRefreshToken(user);

    Assertions.assertDoesNotThrow(() -> tokenProvider.validateRefreshTokenAndGetId(refreshToken));
    Assertions.assertEquals(Boolean.TRUE, redisTemplate.hasKey(refreshToken));
  }

  @Test
  @DisplayName("리프레쉬 토큰 없을 때")
  public void refreshTokenEmptyTest() {
    MyUserDetails user = creator.createAndSaveUser("testman");
    String refreshToken = tokenProvider.create(user);

    Assertions.assertThrows(CredentialNotMatchException.class,
        () -> tokenProvider.validateRefreshTokenAndGetLongId(refreshToken));
  }
}