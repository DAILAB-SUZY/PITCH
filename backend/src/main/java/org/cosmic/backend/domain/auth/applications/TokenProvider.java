package org.cosmic.backend.domain.auth.applications;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.user.domain.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
public class TokenProvider {
    private static final SecretKey KEY = Jwts.SIG.HS512.key().build();
    private final RedisTemplate<String, String> redisTemplate;

    public String create(User user) {
        Date expiredDate = Date.from(
                Instant.now()
                        .plus(30, ChronoUnit.MINUTES)
        );

        return Jwts.builder()
                .signWith(KEY)
                .subject(user.getId().toString())
                .issuer("pitch")
                .issuedAt(new Date())
                .expiration(expiredDate)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date expiredDate = Date.from(
                Instant.now()
                        .plus(3, ChronoUnit.DAYS)
        );

        String refreshToken = Jwts.builder()
                .signWith(KEY)
                .subject(user.getId().toString())
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

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
}
