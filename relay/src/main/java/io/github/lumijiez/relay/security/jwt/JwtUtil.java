package io.github.lumijiez.relay.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret:UltraFuckingSecureRavenKeyTheresNoWayYouCanSolveIt}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public JwtClaims validateAndGetClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String sub = claims.getSubject();
            String username = claims.get("username", String.class);
            String email = claims.get("email", String.class);

            if (sub == null || username == null || email == null) {
                return JwtClaims.failure("Missing claims in token: sub, username, or email");
            }

            if (claims.getExpiration().before(new Date())) {
                return JwtClaims.failure("Token has expired");
            }

            return JwtClaims.success(sub, username, email);
        } catch (JwtException | IllegalArgumentException e) {
            return JwtClaims.failure("Invalid token or unable to parse claims");
        }
    }
}
