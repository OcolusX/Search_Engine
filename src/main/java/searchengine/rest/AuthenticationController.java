package searchengine.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.jwt.JwtAuthenticationResponse;
import searchengine.dto.jwt.LogoutJwtResponse;
import searchengine.dto.jwt.RefreshJwtRequest;
import searchengine.dto.sign.SignInRequest;
import searchengine.dto.sign.SignUpRequest;
import searchengine.services.HttpCookieService;
import searchengine.services.authentication.AuthenticationService;

import java.io.IOException;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final HttpCookieService httpCookieService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request, HttpServletResponse response) {
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.signUp(request);

        response.addCookie(httpCookieService.createJwtAccessCookie(jwtAuthenticationResponse.getAccessToken()));
        response.addCookie(httpCookieService.createJwtRefreshCookie(jwtAuthenticationResponse.getRefreshToken()));

        return jwtAuthenticationResponse;
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request, HttpServletResponse response) {
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.signIn(request);

        response.addCookie(httpCookieService.createJwtAccessCookie(jwtAuthenticationResponse.getAccessToken()));
        response.addCookie(httpCookieService.createJwtRefreshCookie(jwtAuthenticationResponse.getRefreshToken()));

        return jwtAuthenticationResponse;
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Выход из системы")
    @GetMapping("/logout")
    public LogoutJwtResponse logout(HttpServletResponse response) throws IOException {
        Cookie accessCookie = httpCookieService.createJwtAccessCookie("");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        Cookie refreshCookie = httpCookieService.createJwtRefreshCookie("");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        response.sendRedirect("/");
        return new LogoutJwtResponse(HttpStatus.OK, "logout successful");
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получить новый refresh токен из запроса")
    @PostMapping("/refresh")
    public JwtAuthenticationResponse refresh(
            @RequestBody @Valid RefreshJwtRequest jwtRequest,
            HttpServletResponse response
    ) throws IOException {


        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.refresh(jwtRequest.getRefreshToken());
        String accessToken = jwtAuthenticationResponse.getAccessToken();
        String refreshToken = jwtAuthenticationResponse.getRefreshToken();

        response.addCookie(httpCookieService.createJwtAccessCookie(accessToken));
        response.addCookie(httpCookieService.createJwtRefreshCookie(refreshToken));
        response.sendRedirect("/user/");
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получить новый refresh токен из куки")
    @GetMapping("/refresh")
    public JwtAuthenticationResponse refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(HttpCookieService.REFRESH_COOKIE_NAME)) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            response.sendRedirect("/");
            return new JwtAuthenticationResponse("", "");
        }

        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.refresh(refreshToken);
        String accessToken = jwtAuthenticationResponse.getAccessToken();
        refreshToken = jwtAuthenticationResponse.getRefreshToken();

        response.addCookie(httpCookieService.createJwtAccessCookie(accessToken));
        response.addCookie(httpCookieService.createJwtRefreshCookie(refreshToken));
        response.sendRedirect("/user/");
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

}
