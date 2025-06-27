package com.example.demo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private static final int MINUTES_TO_EXPIRY = 5;

    // 256 bit secret (32 bytes) for HMAC256 algorithm
    // Here the 8-bit ascii value of each character is taken as each byte
    private static final String SECRET = "12345678123456781234567812345678";

    // Normally your secret will be a random 256 bits which is base64 encoded for storing
    // There each character is 6 bits and needs to be base64 decoded to bytes
    private static final String BASE64ENCODEDSECRET = "MTIzNDU2NzgxMjM0NTY3ODEyMzQ1Njc4MTIzNDU2Nzg=";

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + MINUTES_TO_EXPIRY * 60 * 1000))
                .signWith(getKey())
                .compact();
    }

    public boolean verifyToken(String jwtToken) {
        var parser = Jwts.parser().verifyWith(getKey()).build();
        try {
            parser.parse(jwtToken);
        }
        catch (Exception e) {
            log.info("Jwt is invalid {} - {}", e.getClass(), e.getMessage());
            return false;
        }
        return true;
    }

    public String getClaim(String token, String key) {
        var parser = Jwts.parser().verifyWith(getKey()).build();
        var claims = parser.parseSignedClaims(token);
        return claims.getPayload().getOrDefault(key, "").toString();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Both will give the same key
    private SecretKey getKeyFromEncoded() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(BASE64ENCODEDSECRET));
    }
}
