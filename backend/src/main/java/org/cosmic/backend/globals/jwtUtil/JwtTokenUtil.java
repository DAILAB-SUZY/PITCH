package org.cosmic.backend.globals.jwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiredMs}")
    private Long expiration;

    public String createJwt(String memberEmail)
    {
        Claims claims = Jwts.claims();
        claims.put("email", memberEmail);//payload

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(expiration)))//
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }

    public String getUserEmail(String token)
    {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email",String.class);
        //토큰의 유효성 검사하여 유효하면 email 바탕으로 repository 정보 가져옴
    }

    public boolean isExpired(String token)
    {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().before(Date.from(Instant.now()));
    }
}
