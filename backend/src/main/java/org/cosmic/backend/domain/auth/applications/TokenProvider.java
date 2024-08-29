package org.cosmic.backend.domain.auth.applications;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.user.domains.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * TokenProvider는 JWT 토큰을 생성하고 검증하는 서비스입니다.
 *
 * 이 클래스는 사용자의 인증 정보를 기반으로 JWT 액세스 토큰과 리프레시 토큰을 생성하고,
 * 리프레시 토큰을 Redis에 저장하며, JWT 토큰을 검증하는 기능을 제공합니다.
 *
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class TokenProvider {
    private static final SecretKey KEY = Jwts.SIG.HS512.key().build();
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 주어진 사용자 정보를 기반으로 액세스 토큰을 생성합니다.
     *
     * @param user 액세스 토큰을 생성할 사용자 객체
     * @return 생성된 JWT 액세스 토큰
     */
    public String create(User user) {
        Date expiredDate = Date.from(
                Instant.now()
                        .plus(30, ChronoUnit.MINUTES)
        );

        return Jwts.builder()
                .signWith(KEY)
                .subject(user.getUserId().toString())
                .issuer("pitch")
                .issuedAt(new Date())
                .expiration(expiredDate)
                .compact();
    }

    /**
     * 주어진 사용자 정보를 기반으로 리프레시 토큰을 생성하고 Redis에 저장합니다.
     *
     * @param user 리프레시 토큰을 생성할 사용자 객체
     * @return 생성된 JWT 리프레시 토큰
     */
    public String createRefreshToken(User user) {
        Date expiredDate = Date.from(
                Instant.now()
                        .plus(3, ChronoUnit.DAYS)
        );

        String refreshToken = Jwts.builder()
                .signWith(KEY)
                .subject(user.getUserId().toString())
                .issuer("pitch")
                .issuedAt(new Date())
                .expiration(expiredDate)
                .compact();

        redisTemplate.opsForValue().set(
                user.getEmail().getEmail(),
                refreshToken,
                expiredDate.getTime(),
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    /**
     * 주어진 JWT 토큰을 검증하고, 토큰에 포함된 사용자 ID를 반환합니다.
     *
     * @param token 검증할 JWT 토큰
     * @return 토큰에 포함된 사용자 ID
     *
     * @throws io.jsonwebtoken.ExpiredJwtException 토큰이 만료된 경우 발생합니다.
     * @throws io.jsonwebtoken.UnsupportedJwtException 토큰 형식이 지원되지 않는 경우 발생합니다.
     * @throws io.jsonwebtoken.MalformedJwtException 토큰 형식이 잘못된 경우 발생합니다.
     */
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
