package searchengine.services.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import searchengine.model.User;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final Integer accessMaxAge;
    private final Integer refreshMaxAge;

    public JwtService(
            @Value("${jwt.secret.key.access.value}") String accessSecretKey,
            @Value("${jwt.secret.key.refresh.value}") String refreshSecretKey,
            @Value("${jwt.secret.key.access.max-age}") Integer accessMaxAge,
            @Value("${jwt.secret.key.access.max-age}") Integer refreshMaxAge) {
        this.accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey));
        this.refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        this.accessMaxAge = accessMaxAge;
        this.refreshMaxAge = refreshMaxAge;
    }

    public String generateAccessToken(UserDetails userDetails) {
        final LocalDateTime now = LocalDateTime.now();
        final Date expiration = Date.from(now.plusSeconds(accessMaxAge).atZone(ZoneId.systemDefault()).toInstant());

        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("email", customUserDetails.getEmail());
            claims.put("username", customUserDetails.getUsername());
            claims.put("role", customUserDetails.getRole());
        }
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(accessSecretKey)
                .claims(claims)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        final LocalDateTime now = LocalDateTime.now();
        final Date expiration = Date.from(now.plusSeconds(refreshMaxAge).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(refreshSecretKey)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessSecretKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshSecretKey);
    }

    private boolean validateToken(String token, SecretKey secret) {
        Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token);
        return true;
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, accessSecretKey);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, refreshSecretKey);
    }

    private Claims getClaims(String token, SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
