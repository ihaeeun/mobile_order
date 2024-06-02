package com.kakaobank.order.common.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Map;

@Component
public class JwtProvider {
    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        var keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String uuid, String userId) {
        var accessToken = Jwts.builder()
                .setClaims(Map.of("uuid", uuid, "userId", userId))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public Claims parseClaims(String accessToken) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    }
}
