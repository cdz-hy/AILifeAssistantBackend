package org.example.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtUtil(String secret, long expirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    public String generateToken(String userId, String username, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("username", username)
                .addClaims(extraClaims == null ? Map.of() : extraClaims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}