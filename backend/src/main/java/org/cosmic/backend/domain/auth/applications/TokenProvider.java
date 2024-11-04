package org.cosmic.backend.domain.auth.applications;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.exceptions.CredentialNotMatchException;
import org.cosmic.backend.domain.user.domains.MyUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * TokenProvider는 JWT 토큰을 생성하고 검증하는 서비스입니다.
 * <p>
 * 이 클래스는 사용자의 인증 정보를 기반으로 JWT 액세스 토큰과 리프레시 토큰을 생성하고, 리프레시 토큰을 Redis에 저장하며, JWT 토큰을 검증하는 기능을 제공합니다.
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class TokenProvider {

  private final RedisTemplate<String, String> redisTemplate;
  @Value(value = "${app.jwt.secret}")
  private String KEY;
  @Value(value = "${app.jwt.access-expiration}")
  private Long ACCESS_EXPIRATION;
  @Value(value = "${app.jwt.refresh-expiration}")
  private Long REFRESH_EXPIRATION;
  @Value(value = "${spring.application.name}")
  private String ISSUER;

  private SecretKey getSignInKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
  }

  private String createToken(MyUserDetails user, Long expiration, TemporalUnit unit) {
    Instant now = Instant.now();
    return Jwts.builder()
        .signWith(getSignInKey())
        .subject(user.getId())
        .issuedAt(new Date(now.getEpochSecond()))
        .expiration(Date.from(now.plus(expiration, unit)))
        .issuer(ISSUER)
        .compact();
  }

  private void saveTokenInRedis(String id, String token) {
    redisTemplate.opsForValue().set(
        id,
        token,
        Instant.now().plus(REFRESH_EXPIRATION, ChronoUnit.DAYS).getEpochSecond(),
        TimeUnit.MILLISECONDS
    );
  }

  /**
   * 주어진 사용자 정보를 기반으로 액세스 토큰을 생성합니다.
   *
   * @param user 액세스 토큰을 생성할 사용자 객체
   * @return 생성된 JWT 액세스 토큰
   */
  public String create(MyUserDetails user) {
    return createToken(user, ACCESS_EXPIRATION, ChronoUnit.MINUTES);
  }

  /**
   * 주어진 사용자 정보를 기반으로 리프레시 토큰을 생성하고 Redis에 저장합니다.
   *
   * @param user 리프레시 토큰을 생성할 사용자 객체
   * @return 생성된 JWT 리프레시 토큰
   */
  public String createRefreshToken(MyUserDetails user) {
    String token = createToken(user, REFRESH_EXPIRATION, ChronoUnit.DAYS);
    saveTokenInRedis(user.getId(), token);
    return token;
  }

  /**
   * 주어진 JWT 토큰을 검증하고, 토큰에 포함된 사용자 ID를 반환합니다.
   *
   * @param token 검증할 JWT 토큰
   * @return 토큰에 포함된 사용자 ID
   * @throws io.jsonwebtoken.ExpiredJwtException     토큰이 만료된 경우 발생합니다.
   * @throws io.jsonwebtoken.UnsupportedJwtException 토큰 형식이 지원되지 않는 경우 발생합니다.
   * @throws io.jsonwebtoken.MalformedJwtException   토큰 형식이 잘못된 경우 발생합니다.
   */
  public String validateAndGetId(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return claims.getSubject();
  }

  public Long validateAndGetLongId(String token) {
    return Long.valueOf(validateAndGetId(token));
  }

  public String validateRefreshTokenAndGetId(String token) {
    String id = validateAndGetId(token);
    if (!redisTemplate.hasKey(id)) {
      throw new CredentialNotMatchException();
    }
    if (!token.equals(redisTemplate.opsForValue().get(id))) {
      throw new CredentialNotMatchException();
    }
    return id;
  }

  public Long validateRefreshTokenAndGetLongId(String token) {
    return Long.valueOf(validateRefreshTokenAndGetId(token));
  }
}
