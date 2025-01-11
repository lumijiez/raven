package io.github.lumijiez.auth.security;
import io.github.lumijiez.auth.domain.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtHelper {

    private final SecretKey key;
    private final long jwtExpirationInMs;

    public JwtHelper(@Value("${jwt.secret:UltraFuckingSecureRavenKeyTheresNoWayYouCanSolveIt}") String secret,
                     @Value("${jwt.expiration:86400000}") long jwtExpirationInMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String generateToken(UUID id, Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(id.toString())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String generateTokenForUser(User user) {
        UUID id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();
        Map<String, Object> claims = Map.of(
                "username", username,
                "email", email
        );
        return generateToken(id, claims);
    }
}
