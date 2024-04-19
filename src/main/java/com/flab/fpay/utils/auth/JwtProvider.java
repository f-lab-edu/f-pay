package com.flab.fpay.utils.auth;

import com.flab.fpay.domain.auth.dto.JwtClaimDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    @Value("${auth.jwt-secret-key}")
    private String jwtSecretKey;
    SecretKey key;

    @PostConstruct
    public void postConstruct() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecretKey));
    }

    public String createToken(JwtClaimDto claim) {
        return Jwts.builder()
                .claim("memberId", claim.getMemberId())
                .claim("deviceId", claim.getDeviceId())
                .issuedAt(claim.getIssuedAt())
                .expiration(claim.getExpiredAt())
                .signWith(key)
                .compact();
    }

    public Claims decodeToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token);

        return claimsJws.getPayload();
    }
}