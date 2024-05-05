package searchengine.services;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class HttpCookieService {

    public static final String ACCESS_COOKIE_NAME = "accessToken";
    public static final String REFRESH_COOKIE_NAME = "refreshToken";
    public static final String ACCESS_COOKIE_PATH = "/user/";
    public static final String REFRESH_COOKIE_PATH = "/auth/";

    private final Integer accessMaxAge;
    private final Integer refreshMaxAge;

    public HttpCookieService(
            @Value("${jwt.secret.key.access.max-age}") Integer accessMaxAge,
            @Value("${jwt.secret.key.access.max-age}") Integer refreshMaxAge) {
        this.accessMaxAge = accessMaxAge;
        this.refreshMaxAge = refreshMaxAge;
    }


    public Cookie createJwtAccessCookie(String token) {
        Cookie cookie = new Cookie(ACCESS_COOKIE_NAME, token);
        cookie.setMaxAge(accessMaxAge);
        cookie.setHttpOnly(true);
        cookie.setPath(ACCESS_COOKIE_PATH);
        return cookie;
    }

    public Cookie createJwtRefreshCookie(String token) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, token);
        cookie.setMaxAge(refreshMaxAge);
        cookie.setHttpOnly(true);
        cookie.setPath(REFRESH_COOKIE_PATH);
        return cookie;
    }

}
