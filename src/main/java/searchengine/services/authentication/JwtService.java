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

    public static final String ACCESS_COOKIE_NAME = "accessToken";
    public static final String REFRESH_COOKIE_NAME = "refreshToken";
    public static final String ACCESS_COOKIE_PATH = "/user/";
    public static final String REFRESH_COOKIE_PATH = "/auth/";
    public static final int ACCESS_MAX_AGE_SECONDS = 10;
    public static final int REFRESH_MAX_AGE_SECONDS = 3600;

    public JwtService(
            @Value("${jwt.secret.key.access}") String accessSecretKey,
            @Value("${jwt.secret.key.refresh}") String refreshSecretKey) {
        this.accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey));
        this.refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
    }

    public String generateAccessToken(UserDetails userDetails) {
        final LocalDateTime now = LocalDateTime.now();
        final Date expiration = Date.from(now.plusSeconds(ACCESS_MAX_AGE_SECONDS).atZone(ZoneId.systemDefault()).toInstant());

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
        final Date expiration = Date.from(now.plusSeconds(REFRESH_MAX_AGE_SECONDS).atZone(ZoneId.systemDefault()).toInstant());

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

    public Cookie createJwtAccessCookie(String token) {
        Cookie cookie = new Cookie(ACCESS_COOKIE_NAME, token);
        cookie.setMaxAge(ACCESS_MAX_AGE_SECONDS);
        cookie.setHttpOnly(true);
        cookie.setPath(ACCESS_COOKIE_PATH);
        return cookie;
    }

    public Cookie createJwtRefreshCookie(String token) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, token);
        cookie.setMaxAge(REFRESH_MAX_AGE_SECONDS);
        cookie.setHttpOnly(true);
        cookie.setPath(REFRESH_COOKIE_PATH);
        return cookie;
    }

}
